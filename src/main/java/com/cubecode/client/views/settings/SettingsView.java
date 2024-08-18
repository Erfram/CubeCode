package com.cubecode.client.views.settings;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import imgui.ImGui;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class SettingsView extends View {
    private final int viewWidth = 960;
    private final int viewHeight = 540;

    private Map<String, Map<String, Runnable>> settings = new LinkedHashMap<>();

    private Map<String, Runnable> general = new LinkedHashMap<>();

    private List<String> themes = Arrays.asList("ImGui", "CubeCode");

    private int selectedSettingIndex = -1;

    private Runnable selectedSetting = () -> {};

    @Override
    public void init() {
        HashMap<String, Runnable> objectObjectHashMap = new LinkedHashMap<>();
        objectObjectHashMap.put("ff", () -> {});

        HashMap<String, Runnable> objectHashMap = new LinkedHashMap<>();
        objectHashMap.put("ff", () -> {});
        objectHashMap.put("fffew", () -> {});
        objectHashMap.put("ffewrf", () -> {});

        general.put("Appearance", this::renderAppearanceContent);
        general.put("Lox", () -> {});

        settings.put("General", general);
        settings.put("General 2", objectObjectHashMap);
        settings.put("General 3", objectHashMap);

        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public String getName() {
        return String.format("Settings ##%s", uniqueID);
    }

    @Override
    public void render() {
        Window.create()
            .title(getName())
            .callback(() -> {
                CubeImGui.beginChild("Settings Pane", 200, 0, false, this::renderSettingsPane);

                ImGui.sameLine();

                CubeImGui.beginChild("Content Pane", 0, 0, true, selectedSetting);
            })
            .render(this);
    }

    private void renderSettingsPane() {
        int globalIndex = 0;
        for (Map.Entry<String, Map<String, Runnable>> category : settings.entrySet()) {
            ImGui.spacing();
            ImGui.spacing();

            CubeImGui.textMutable(Text.translatable(category.getKey()).formatted(Formatting.GRAY));

            ImGui.spacing();
            ImGui.spacing();

            if (ImGui.beginChild("setting"+category.getKey(), 0, (32 * category.getValue().size()) > 32 ? 32 * category.getValue().size() * 0.82f : 32 * category.getValue().size(), true)) {
                for (Map.Entry<String, Runnable> setting : category.getValue().entrySet()) {

                        if (ImGui.selectable(" " + setting.getKey(), selectedSettingIndex == globalIndex, ImGuiSelectableFlags.None)) {
                            selectedSettingIndex = globalIndex;
                            selectedSetting = setting.getValue();
                        }
                        globalIndex++;

                }
            }
            ImGui.endChild();
        }
    }

    private void renderAppearanceContent() {
        ImGui.text("Theme: ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Themes", 0, themes.toArray(new String[0]), (theme) -> {

        });
    }
}

package com.cubecode.client.views.settings;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.imgui.fonts.FontManager;
import com.cubecode.client.imgui.themes.CubeTheme;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImInt;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class SettingsView extends View {
    private final int viewWidth = 960;
    private final int viewHeight = 540;

    private final Map<String, Map<String, Runnable>> settings = new LinkedHashMap<>();

    private int selectedSettingIndex = -1;

    private Runnable selectedSetting = () -> {};

    private final List<String> fonts;
    private final List<String> themes;

    public SettingsView() {
        fonts = FontManager.getFontNames();
        themes = CubeCodeClient.themeManager.getThemeNames();
    }

    @Override
    public void init() {
        Map<String, Runnable> general = new LinkedHashMap<>();

        general.put("Appearance", this::renderAppearanceContent);

        settings.put("General", general);

        HashMap<String, Runnable> general2 = new LinkedHashMap<>();
        general2.put("ff", () -> {});

        settings.put("General 2", general2);

        HashMap<String, Runnable> general3 = new LinkedHashMap<>();
        general3.put("ff", () -> {});

        settings.put("General 3", general3);

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

            for (Map.Entry<String, Runnable> setting : category.getValue().entrySet()) {
                if (ImGui.selectable(" " + setting.getKey(), selectedSettingIndex == globalIndex, ImGuiSelectableFlags.None)) {
                    selectedSettingIndex = globalIndex;
                    selectedSetting = setting.getValue();
                }
                globalIndex++;

            }
        }
    }

    private void renderAppearanceContent() {
        ImGui.text("Theme: ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Themes", themes.indexOf(CubeCodeConfig.getSettingsConfig().general.appearance.theme), themes.toArray(new String[0]), (theme) -> {
            CubeCodeConfig.getSettingsConfig().general.appearance.theme = themes.get(((ImInt) this.getVariable("##Themes" + this.getUniqueID())).get());
            CubeCodeClient.themeManager.currentTheme = themes.get(((ImInt) this.getVariable("##Themes" + this.getUniqueID())).get());
        });

        ImGui.text("Font: ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Fonts", fonts.indexOf(CubeCodeConfig.getSettingsConfig().general.appearance.font), fonts.toArray(new String[0]), (font) -> {
            CubeCodeConfig.getSettingsConfig().general.appearance.font = fonts.get(((ImInt) this.getVariable("##Fonts" + this.getUniqueID())).get());
            CubeCodeClient.fontManager.currentFontName = fonts.get(((ImInt) this.getVariable("##Fonts" + this.getUniqueID())).get());
        });

        ImGui.sameLine(0, 0);
        ImGui.image(Icons.INFO, 21, 21);
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text("После добавления шрифта в конфиг, перезагрузите игру.");
            ImGui.endTooltip();
        }
    }

    @Override
    public void onClose() {
        CubeCodeConfig.saveConfig();
    }
}
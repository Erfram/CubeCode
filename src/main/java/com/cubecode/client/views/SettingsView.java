package com.cubecode.client.views;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.imgui.fonts.FontManager;
import com.cubecode.client.imgui.themes.CubeTheme;
import com.cubecode.client.imgui.themes.ThemeManager;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImInt;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class SettingsView extends View {
    private final int viewWidth = 960;
    private final int viewHeight = 540;

    private Runnable selectedSetting = () -> {};

    private final List<String> fonts;
    private final List<String> themes;

    public SettingsView() {
        fonts = FontManager.getFontNames();
        themes = CubeCodeClient.themeManager.getThemeNames();
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.dashboard.settings.title").getString()+" ##%s", uniqueID);
    }

    @Override
    public void render() {
        Window.create()
                .title(getName())
                .onExit(CubeCodeConfig::saveConfig)
                .callback(() -> {
                    CubeImGui.beginChild("Settings Pane", 200, 0, false, this::renderSettingsPane);

                    ImGui.sameLine();

                    CubeImGui.beginChild("Content Pane", 0, 0, true, selectedSetting);
                })
                .render(this);
    }

    private void renderSettingsPane() {
        CubeImGui.treeNodeEx(Text.translatable("imgui.cubecode.windows.settings.general").getString(), Icons.FLAG, ImGuiTreeNodeFlags.SpanAvailWidth, () -> {
            CubeImGui.selectable(Text.translatable("imgui.cubecode.windows.settings.appearance").getString(), false, Icons.APPEARANCE, 0, () -> {
                selectedSetting = this::renderAppearanceContent;
            });
        });
    }

    private void renderAppearanceContent() {
        ImGui.image(Icons.THEME.getGlId(), 16, 16);
        ImGui.sameLine();

        ImGui.text(Text.translatable("imgui.cubecode.windows.settings.appearance.theme").getString()+": ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Themes", themes.indexOf(CubeCodeConfig.getSettingsConfig().general.appearance.theme), themes.toArray(new String[0]), (theme) -> {
            String th = themes.get(((ImInt) this.getVariable("##Themes" + this.getUniqueID())).get());
            CubeCodeConfig.getSettingsConfig().general.appearance.theme = th;
            CubeCodeClient.themeManager.currentTheme = CubeCodeClient.themeManager.getTheme(th);
        });

        ImGui.image(Icons.LLAMA.getGlId(), 16, 16);
        ImGui.sameLine();

        ImGui.text(Text.translatable("imgui.cubecode.windows.settings.appearance.font").getString()+": ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Fonts", fonts.indexOf(CubeCodeConfig.getSettingsConfig().general.appearance.font), fonts.toArray(new String[0]), (font) -> {
            CubeCodeConfig.getSettingsConfig().general.appearance.font = fonts.get(((ImInt) this.getVariable("##Fonts" + this.getUniqueID())).get());
            CubeCodeClient.fontManager.currentFontName = fonts.get(((ImInt) this.getVariable("##Fonts" + this.getUniqueID())).get());
        });

        ImGui.sameLine();
        ImGui.image(Icons.INFO.getGlId(), 21, 21);
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
package com.cubecode.client.views;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.client.imgui.fonts.FontManager;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

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
        float posX = (MinecraftClient.getInstance().getWindow().getWidth() - viewWidth) * 0.5f;
        float posY = (MinecraftClient.getInstance().getWindow().getHeight() - viewHeight) * 0.5f;

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
                .onExit(this::onClose)
                .callback(() -> {
                    float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
                    float variableSplitter = this.getVariable("splitter") == null ? 100f : this.getVariable("splitter");


                    CubeImGui.beginChild("Settings Pane", availableWidth * variableSplitter, 0, false, this::renderSettingsPane);

                    ImGui.sameLine();
                    CubeImGui.verticalSplitter(this, "splitter", 4, availableWidth, ImGui.getItemRectMaxY(), 0.3f, 0.1f, 0.9f);
                    ImGui.sameLine();

                    CubeImGui.beginChild("Content Pane", 0, 0, true, selectedSetting);
                })
                .render(this);
    }

    private void renderSettingsPane() {
        CubeImGui.treeNodeEx(Text.translatable("imgui.cubecode.windows.settings.general").getString(), Icons.FLAG, ImGuiTreeNodeFlags.SpanAvailWidth, () -> {
            CubeImGui.selectable(Text.translatable("imgui.cubecode.windows.settings.appearance").getString(), false, Icons.APPEARANCE, 0, () -> {
                this.selectedSetting = this::renderAppearanceContent;
            });
            CubeImGui.selectable("IDEA", false, Icons.SERVER, 0, () -> {
                this.selectedSetting = this::renderIDEAContent;
            });
        }, ImGui.getFontSize());
    }

    private void renderAppearanceContent() {
        float fontSize = ImGui.getFontSize();
        ImGui.image(Icons.THEME.getGlId(), fontSize, fontSize);
        ImGui.sameLine();

        ImGui.text(Text.translatable("imgui.cubecode.windows.settings.appearance.theme").getString()+": ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Themes", themes.indexOf(CubeCodeConfig.getSettingsConfig().general.appearance.theme), themes.toArray(new String[0]), (theme) -> {
            String th = themes.get(((ImInt) this.getVariable("##Themes" + this.getUniqueID())).get());
            CubeCodeConfig.getSettingsConfig().general.appearance.theme = th;
            CubeCodeClient.themeManager.currentTheme = CubeCodeClient.themeManager.getTheme(th);
        });

        ImGui.image(Icons.LLAMA.getGlId(), fontSize, fontSize);
        ImGui.sameLine();

        ImGui.text(Text.translatable("imgui.cubecode.windows.settings.appearance.font").getString()+": ");
        ImGui.sameLine();

        CubeImGui.combo(this, "##Fonts", fonts.indexOf(CubeCodeConfig.getSettingsConfig().general.appearance.font), fonts.toArray(new String[0]), (font) -> {
            CubeCodeConfig.getSettingsConfig().general.appearance.font = fonts.get(((ImInt) this.getVariable("##Fonts" + this.getUniqueID())).get());
            CubeCodeClient.fontManager.currentFontName = fonts.get(((ImInt) this.getVariable("##Fonts" + this.getUniqueID())).get());
        });

        ImGui.sameLine();
        ImGui.image(Icons.INFO.getGlId(), fontSize, fontSize);
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.text(Text.translatable("imgui.cubecode.windows.settings.appearance.font.tooltip").getString());
            ImGui.endTooltip();
        }

        ImGui.text(Text.translatable("imgui.cubecode.windows.settings.appearance.fontSize").getString()+": ");
        ImGui.sameLine();

        this.putVariable("fontSize", new float[]{CubeCodeConfig.getFontSize() / 64});
        //ImGui.sliderFloat("##FontSize", this.getVariable("fontSize"), 0.5f, 100.0f);
        ImGui.sliderFloat("##FontSize", this.getVariable("fontSize"), 0.1f, 2.0f, "%.2f", ImGuiSliderFlags.Logarithmic);
        if (ImGui.isItemDeactivatedAfterEdit()) {
            this.updateSettings();
        }
    }

    private void renderIDEAContent() {
        String tabSizeId = "tabSize_" + this.getUniqueID();
        String showWhitespacesId = "showWhitespaces_"+this.getUniqueID();
        String readOnlyId = "readOnly_" + this.getUniqueID();

        this.putVariable(tabSizeId, new int[]{CubeCodeConfig.getIdeaSettingsConfig().tabSize});
        this.putVariable(showWhitespacesId, new ImBoolean(CubeCodeConfig.getIdeaSettingsConfig().showWhitespaces));
        this.putVariable(readOnlyId, new ImBoolean(CubeCodeConfig.getIdeaSettingsConfig().readOnly));

        ImGui.sliderInt("tabSize", this.getVariable(tabSizeId), 0, 8);
        ImGui.checkbox("showWhitespaces", this.getVariable(showWhitespacesId));
        ImGui.checkbox("readOnly", this.getVariable(readOnlyId));
    }

    @Override
    public void onClose() {
        this.updateSettings();
    }

    public void updateSettings() {
        CubeCodeConfig.saveConfig();

        String tabSizeId = "tabSize_" + this.getUniqueID();
        String showWhitespacesId = "showWhitespaces_"+this.getUniqueID();
        String readOnlyId = "readOnly_" + this.getUniqueID();

        int[] tabSize = this.getVariable(tabSizeId);
        ImBoolean showWhitespaces = this.getVariable(showWhitespacesId);
        ImBoolean readOnly = this.getVariable(readOnlyId);

        float[] fontSize = this.getVariable("fontSize");

        if (tabSize != null) {
            CubeCodeConfig.getIdeaSettingsConfig().tabSize = tabSize[0];
        }

        if (showWhitespaces != null) {
            CubeCodeConfig.getIdeaSettingsConfig().showWhitespaces = showWhitespaces.get();
        }

        if (readOnly != null) {
            CubeCodeConfig.getIdeaSettingsConfig().readOnly = readOnly.get();
        }

        if (fontSize != null) {
            CubeCodeConfig.setFontSize(fontSize[0]);
        }
    }
}
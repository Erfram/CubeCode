package com.cubecode.client.views.settings;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.utils.Icons;
import imgui.ImGui;
import imgui.flag.ImGuiDir;
import net.minecraft.text.Text;

public class WindowView extends View {
    private static final int VIEW_WIDTH = 250;
    private static final int VIEW_HEIGHT = 550;

    private static final int[] TITLE_TEXT_COLOR = {255, 207, 64, 255};

    private static final int COLOR_PICKER_WIDTH = 200;

    private final CubeCodeConfig.WindowConfig windowConfig = CubeCodeConfig.getWindowConfig();

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.settings.window.name").getString() + "##%s", uniqueID);
    }

    @Override
    public void init() {
        float posX = (windowWidth - VIEW_WIDTH) * 0.5f;
        float posY = (windowHeight - VIEW_HEIGHT) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(VIEW_WIDTH, VIEW_HEIGHT);
    }

    //ARROW BUTTONS
    private int bgButton = ImGuiDir.Right;

    private int titleActiveButton = ImGuiDir.Right;
    private int titleNotActiveButton = ImGuiDir.Right;
    private int titleColapsedButton = ImGuiDir.Right;

    private int borderButton = ImGuiDir.Right;

    private int buttonButton = ImGuiDir.Right;
    private int buttonActiveButton = ImGuiDir.Right;
    private int buttonHoveredButton = ImGuiDir.Right;

    private int frameBgButton = ImGuiDir.Right;
    private int frameBgActiveButton = ImGuiDir.Right;
    private int frameBgHoveredButton = ImGuiDir.Right;

    private int sliderGrabButton = ImGuiDir.Right;
    private int sliderGrabActiveButton = ImGuiDir.Right;

    //COLORS
    private float[] bgColor = windowConfig.bgColor.clone();

    private float[] titleColor = windowConfig.titleColor.clone();
    private float[] titleActiveColor = windowConfig.titleActiveColor.clone();
    private float[] titleCollapsedColor = windowConfig.titleCollapsedColor.clone();

    private float[] borderColor = windowConfig.borderColor.clone();

    private float[] buttonColor = windowConfig.buttonColor.clone();
    private float[] buttonActiveColor = windowConfig.buttonActiveColor.clone();
    private float[] buttonHoveredColor = windowConfig.buttonHoveredColor.clone();

    private float[] frameBgColor = windowConfig.frameBgColor.clone();
    private float[] frameBgActiveColor = windowConfig.frameBgActiveColor.clone();
    private float[] frameBgHoveredColor = windowConfig.frameBgHoveredColor.clone();

    private float[] sliderGrabColor = windowConfig.sliderGrabColor.clone();
    private float[] sliderGrabActiveColor = windowConfig.sliderGrabActiveColor.clone();

    @Override
    public void render() {
        Window.create()
                .title(getName())
                .callback(() -> {
                    renderBackground();
                    ImGui.separator();
                    renderTitle();
                    ImGui.separator();
                    renderBorder();
                    ImGui.separator();
                    renderButton();
                    ImGui.separator();
                    renderFrameBg();
                    ImGui.separator();
                    renderSliderGrab();
                    ImGui.separator();

                    CubeImGui.button(Text.translatable("imgui.cubecode.settings.window.reset.button.title").getString(), () -> {
                        bgColor = CubeCodeConfig.DEFAULT_BG_COLOR.clone();
                        titleColor = CubeCodeConfig.DEFAULT_TITLE_COLOR.clone();
                        titleActiveColor = CubeCodeConfig.DEFAULT_TITLE_ACTIVE_COLOR.clone();
                        titleCollapsedColor = CubeCodeConfig.DEFAULT_TITLE_COLLAPSED_COLOR.clone();
                        titleCollapsedColor = CubeCodeConfig.DEFAULT_TITLE_COLLAPSED_COLOR.clone();
                        borderColor = CubeCodeConfig.DEFAULT_BORDER_COLOR.clone();
                        buttonColor = CubeCodeConfig.DEFAULT_BUTTON_COLOR.clone();
                        buttonActiveColor = CubeCodeConfig.DEFAULT_BUTTON_ACTIVE_COLOR.clone();
                        buttonHoveredColor = CubeCodeConfig.DEFAULT_BUTTON_HOVERED_COLOR.clone();
                        frameBgColor = CubeCodeConfig.DEFAULT_FRAME_BG_COLOR.clone();
                        frameBgActiveColor = CubeCodeConfig.DEFAULT_FRAME_BG_ACTIVE_COLOR.clone();
                        frameBgHoveredColor = CubeCodeConfig.DEFAULT_FRAME_BG_HOVERED_COLOR.clone();

                        sliderGrabColor = CubeCodeConfig.DEFAULT_SLIDER_GRAB_COLOR.clone();
                        sliderGrabActiveColor = CubeCodeConfig.DEFAULT_SLIDER_GRAB_ACTIVE_COLOR.clone();

                        CubeCodeConfig.saveConfig();
                    });

                    CubeImGui.button(Text.translatable("imgui.cubecode.settings.window.apply.button.title").getString(), () -> {
                        CubeCodeConfig.getWindowConfig().bgColor = bgColor.clone();
                        CubeCodeConfig.getWindowConfig().titleColor = titleColor.clone();
                        CubeCodeConfig.getWindowConfig().titleActiveColor = titleActiveColor.clone();
                        CubeCodeConfig.getWindowConfig().titleCollapsedColor = titleCollapsedColor.clone();
                        CubeCodeConfig.getWindowConfig().borderColor = borderColor.clone();
                        CubeCodeConfig.getWindowConfig().buttonColor = buttonColor.clone();
                        CubeCodeConfig.getWindowConfig().buttonHoveredColor = buttonHoveredColor.clone();
                        CubeCodeConfig.getWindowConfig().frameBgColor = frameBgColor.clone();
                        CubeCodeConfig.getWindowConfig().frameBgActiveColor = frameBgActiveColor.clone();
                        CubeCodeConfig.getWindowConfig().frameBgHoveredColor = frameBgHoveredColor.clone();
                        CubeCodeConfig.getWindowConfig().sliderGrabColor = sliderGrabColor.clone();
                        CubeCodeConfig.getWindowConfig().sliderGrabActiveColor = sliderGrabActiveColor.clone();

                        CubeCodeConfig.saveConfig();
                    });
                })
                .render(this);
    }

    private void renderBackground() {
        ImGui.textColored(255, 207, 64, 255, Text.translatable("imgui.cubecode.settings.window.background.title").getString());
        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.color").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##BgColor", bgButton)) {
            bgButton = bgButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("backgroundReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            bgColor = CubeCodeConfig.DEFAULT_BG_COLOR.clone();
        }
        ImGui.popID();

        if (bgButton == ImGuiDir.Down) {
            renderColorPicker("BgColor", bgColor);
        }
    }

    private void renderTitle() {
        ImGui.textColored(TITLE_TEXT_COLOR[0], TITLE_TEXT_COLOR[1], TITLE_TEXT_COLOR[2], TITLE_TEXT_COLOR[3],Text.translatable("imgui.cubecode.settings.window.title.title").getString());
        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.color").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##TitleActiveColor", titleActiveButton)) {
            titleActiveButton = titleActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("titleActiveReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            System.out.println("lox");
            titleColor = CubeCodeConfig.DEFAULT_TITLE_COLOR.clone();
        }
        ImGui.popID();

        if (titleActiveButton == ImGuiDir.Down) {
            renderColorPicker("TitleActive", titleColor);
        }

        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorNotActive").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##TitleNotActiveColor", titleNotActiveButton)) {
            titleNotActiveButton = titleNotActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("titleNotActiveReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            titleActiveColor = CubeCodeConfig.DEFAULT_TITLE_ACTIVE_COLOR.clone();
        }
        ImGui.popID();

        if (titleNotActiveButton == ImGuiDir.Down) {
            renderColorPicker("TitleNotActive", titleActiveColor);
        }

        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorCollapsed").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##TitleColapsedColor", titleColapsedButton)) {
            titleColapsedButton = titleColapsedButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("titleCollapsedReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            titleCollapsedColor = CubeCodeConfig.DEFAULT_TITLE_COLLAPSED_COLOR.clone();
        }
        ImGui.popID();

        if (titleColapsedButton == ImGuiDir.Down) {
            renderColorPicker("TitleCollapsed", titleCollapsedColor);
        }
    }

    public void renderBorder() {
        ImGui.textColored(TITLE_TEXT_COLOR[0], TITLE_TEXT_COLOR[1], TITLE_TEXT_COLOR[2], TITLE_TEXT_COLOR[3], Text.translatable("imgui.cubecode.settings.window.border.title").getString());
        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.color").getString());
        ImGui.sameLine();

        if (ImGui.arrowButton("##BorderColor", borderButton)) {
            borderButton = borderButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("borderReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            borderColor = CubeCodeConfig.DEFAULT_BORDER_COLOR.clone();
        }
        ImGui.popID();

        if (borderButton == ImGuiDir.Down) {
            renderColorPicker("Border", borderColor);
        }
    }

    public void renderButton() {
        ImGui.textColored(TITLE_TEXT_COLOR[0], TITLE_TEXT_COLOR[1], TITLE_TEXT_COLOR[2], TITLE_TEXT_COLOR[3], Text.translatable("imgui.cubecode.settings.window.button.title").getString());

        ImGui.spacing();

        ImGui.text(Text.translatable("imgui.cubecode.settings.window.color").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##ButtonColor", buttonButton)) {
            buttonButton = buttonButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("buttonReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            buttonColor = CubeCodeConfig.DEFAULT_BUTTON_COLOR.clone();
        }
        ImGui.popID();

        if (buttonButton == ImGuiDir.Down) {
            renderColorPicker("Button", buttonColor);
        }

        ImGui.spacing();

        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorActive").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##ButtonActiveColor", buttonActiveButton)) {
            buttonActiveButton = buttonActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("buttonActiveReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            buttonActiveColor = CubeCodeConfig.DEFAULT_BUTTON_ACTIVE_COLOR.clone();
        }
        ImGui.popID();

        if (buttonActiveButton == ImGuiDir.Down) {
            renderColorPicker("ButtonActive", buttonActiveColor);
        }

        ImGui.spacing();

        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorHovered").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##ButtonHoveredColor", buttonHoveredButton)) {
            buttonHoveredButton = buttonHoveredButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("buttonHoveredReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            buttonHoveredColor = CubeCodeConfig.DEFAULT_BUTTON_HOVERED_COLOR.clone();
        }
        ImGui.popID();

        if (buttonHoveredButton == ImGuiDir.Down) {
            renderColorPicker("ButtonHovered", buttonHoveredColor);
        }
    }

    public void renderFrameBg() {
        ImGui.textColored(TITLE_TEXT_COLOR[0], TITLE_TEXT_COLOR[1], TITLE_TEXT_COLOR[2], TITLE_TEXT_COLOR[3], Text.translatable("imgui.cubecode.settings.window.frameBackground.title").getString());
        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.color").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##FrameBgColor", frameBgButton)) {
            frameBgButton = frameBgButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("frameBgReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            frameBgColor = CubeCodeConfig.DEFAULT_FRAME_BG_COLOR.clone();
        }
        ImGui.popID();

        if (frameBgButton == ImGuiDir.Down) {
            renderColorPicker("FrameBackground", frameBgColor);
        }

        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorActive").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##FrameBgActiveColor", frameBgActiveButton)) {
            frameBgActiveButton = frameBgActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("frameBgActiveReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            frameBgActiveColor = CubeCodeConfig.DEFAULT_FRAME_BG_ACTIVE_COLOR.clone();
        }
        ImGui.popID();

        if (frameBgActiveButton == ImGuiDir.Down) {
            renderColorPicker("FrameBackgroundActive", frameBgActiveColor);
        }

        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorHovered").getString());
        ImGui.sameLine();
        if (ImGui.arrowButton("##FrameBgHoveredColor", frameBgHoveredButton)) {
            frameBgHoveredButton = frameBgHoveredButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("frameBgHoveredReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            frameBgHoveredColor = CubeCodeConfig.DEFAULT_FRAME_BG_HOVERED_COLOR.clone();
        }
        ImGui.popID();

        if (frameBgHoveredButton == ImGuiDir.Down) {
            renderColorPicker("FrameBackgroundHovered", frameBgHoveredColor);
        }
    }

    public void renderSliderGrab() {
        ImGui.textColored(TITLE_TEXT_COLOR[0], TITLE_TEXT_COLOR[1], TITLE_TEXT_COLOR[2], TITLE_TEXT_COLOR[3], Text.translatable("imgui.cubecode.settings.window.sliderGrab.title").getString());
        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.color").getString());
        ImGui.sameLine();

        if (ImGui.arrowButton("##SliderGrabColor", sliderGrabButton)) {
            sliderGrabButton = sliderGrabButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("sliderGrabReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            sliderGrabColor = CubeCodeConfig.DEFAULT_SLIDER_GRAB_COLOR.clone();
        }
        ImGui.popID();

        if (sliderGrabButton == ImGuiDir.Down) {
            renderColorPicker("SliderGrab", sliderGrabColor);
        }

        ImGui.spacing();
        ImGui.text(Text.translatable("imgui.cubecode.settings.window.colorActive").getString());
        ImGui.sameLine();

        if (ImGui.arrowButton("##SliderGrabActiveColor", sliderGrabActiveButton)) {
            sliderGrabActiveButton = sliderGrabActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }
        ImGui.sameLine();
        ImGui.pushID("sliderGrabActiveReset");
        if (ImGui.imageButton(Icons.RESET, 16, 16)) {
            sliderGrabActiveColor = CubeCodeConfig.DEFAULT_SLIDER_GRAB_ACTIVE_COLOR.clone();
        }
        ImGui.popID();

        if (sliderGrabActiveButton == ImGuiDir.Down) {
            renderColorPicker("SliderGrabActive", sliderGrabActiveColor);
        }
    }

    public void renderColorPicker(String label, float[] color) {
        ImGui.pushItemWidth(WindowView.COLOR_PICKER_WIDTH);
        ImGui.colorPicker4("##CP"+label, color);
        ImGui.popItemWidth();
    }
}
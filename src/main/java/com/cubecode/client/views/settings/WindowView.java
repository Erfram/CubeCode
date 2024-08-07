package com.cubecode.client.views.settings;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import imgui.ImGui;
import imgui.flag.ImGuiColorEditFlags;
import imgui.flag.ImGuiDir;

public class WindowView extends View {
    private final int viewWidth = 250;
    private final int viewHeight = 550;

    private final CubeCodeConfig.WindowConfig windowConfig = CubeCodeConfig.getWindowConfig();

    @Override
    public String getName() {
        return String.format("Window Settings##%s", uniqueID);
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
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

                    CubeImGui.button("Reset", () -> {
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

                        CubeCodeConfig.saveConfig();
                    });

                    CubeImGui.button("Apply", () -> {
                        CubeCodeConfig.getWindowConfig().bgColor = bgColor.clone();
                        CubeCodeConfig.getWindowConfig().titleColor = titleColor.clone();
                        CubeCodeConfig.getWindowConfig().titleActiveColor = titleActiveColor.clone();
                        CubeCodeConfig.getWindowConfig().titleCollapsedColor = titleCollapsedColor.clone();
                        CubeCodeConfig.getWindowConfig().borderColor = borderColor.clone();
                        CubeCodeConfig.getWindowConfig().buttonColor = buttonColor.clone();
                        CubeCodeConfig.getWindowConfig().buttonActiveColor = buttonActiveColor.clone();
                        CubeCodeConfig.getWindowConfig().buttonHoveredColor = buttonHoveredColor.clone();
                        CubeCodeConfig.getWindowConfig().frameBgColor = frameBgColor.clone();
                        CubeCodeConfig.getWindowConfig().frameBgActiveColor = frameBgActiveColor.clone();
                        CubeCodeConfig.getWindowConfig().frameBgHoveredColor = frameBgHoveredColor.clone();

                        CubeCodeConfig.saveConfig();
                    });

                    renderSliderGrab();
                })
                .render(this);
    }

    private void renderBackground() {
        ImGui.textColored(255, 207, 64, 255, "Background");
        ImGui.spacing();
        ImGui.text("Color");
        ImGui.sameLine();
        if (ImGui.arrowButton("##BgColor", bgButton)) {
            bgButton = bgButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (bgButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPBgColor", bgColor, ImGuiColorEditFlags.None);
            ImGui.popItemWidth();
        }
    }

    private void renderTitle() {
        ImGui.textColored(255, 207, 64, 255,"Title");
        ImGui.spacing();
        ImGui.text("Color");
        ImGui.sameLine();
        if (ImGui.arrowButton("##TitleActiveColor", titleActiveButton)) {
            titleActiveButton = titleActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (titleActiveButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPTitleActive", titleActiveColor);
            ImGui.popItemWidth();
        }

        ImGui.spacing();
        ImGui.text("Color not active");
        ImGui.sameLine();
        if (ImGui.arrowButton("##TitleNotActiveColor", titleNotActiveButton)) {
            titleNotActiveButton = titleNotActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (titleNotActiveButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPTitleNotActive", titleColor);
            ImGui.popItemWidth();
        }

        ImGui.spacing();
        ImGui.text("Color collapsed");
        ImGui.sameLine();
        if (ImGui.arrowButton("##TitleColapsedColor", titleColapsedButton)) {
            titleColapsedButton = titleColapsedButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (titleColapsedButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPTitleNotActive", titleCollapsedColor);
            ImGui.popItemWidth();
        }
    }

    public void renderBorder() {
        ImGui.textColored(255, 207, 64, 255, "Border");
        ImGui.spacing();
        ImGui.text("Color");
        ImGui.sameLine();

        if (ImGui.arrowButton("##BorderColor", borderButton)) {
            borderButton = borderButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (borderButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPBorder", borderColor);
            ImGui.popItemWidth();
        }
    }

    public void renderButton() {
        ImGui.textColored(255, 207, 64, 255, "Button");

        ImGui.spacing();

        ImGui.text("Color");
        ImGui.sameLine();
        if (ImGui.arrowButton("##ButtonColor", buttonButton)) {
            buttonButton = buttonButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (buttonButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPButton", buttonColor);
            ImGui.popItemWidth();
        }

        ImGui.spacing();

        ImGui.text("Color active");
        ImGui.sameLine();
        if (ImGui.arrowButton("##ButtonActiveColor", buttonActiveButton)) {
            buttonActiveButton = buttonActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (buttonActiveButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPButtonActive", buttonActiveColor);
            ImGui.popItemWidth();
        }

        ImGui.spacing();

        ImGui.text("Color hovered");
        ImGui.sameLine();
        if (ImGui.arrowButton("##ButtonHoveredColor", buttonHoveredButton)) {
            buttonHoveredButton = buttonHoveredButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (buttonHoveredButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPButtonHovered", buttonHoveredColor);
            ImGui.popItemWidth();
        }
    }

    public void renderFrameBg() {
        ImGui.textColored(255, 207, 64, 255, "Frame Background");
        ImGui.spacing();
        ImGui.text("Color");
        ImGui.sameLine();
        if (ImGui.arrowButton("##FrameBgColor", frameBgButton)) {
            frameBgButton = frameBgButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (frameBgButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPFrameBackground", frameBgColor);
            ImGui.popItemWidth();
        }

        ImGui.spacing();
        ImGui.text("Color active");
        ImGui.sameLine();
        if (ImGui.arrowButton("##FrameBgActiveColor", frameBgActiveButton)) {
            frameBgActiveButton = frameBgActiveButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (frameBgActiveButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPFrameBackgroundActive", frameBgActiveColor);
            ImGui.popItemWidth();
        }

        ImGui.spacing();
        ImGui.text("Color hovered");
        ImGui.sameLine();
        if (ImGui.arrowButton("##FrameBgHoveredColor", frameBgHoveredButton)) {
            frameBgHoveredButton = frameBgHoveredButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (frameBgHoveredButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPFrameBackgroundHovered", frameBgHoveredColor);
            ImGui.popItemWidth();
        }
    }

    public void renderSliderGrab() {
        ImGui.setCursorPos(225, 30);
        ImGui.textColored(255, 207, 64, 255, "Slider Grab");
        ImGui.spacing();
        ImGui.setCursorPos(225, 55);
        ImGui.text("Color");
        ImGui.sameLine();

        if (ImGui.arrowButton("##SliderGrabColor", sliderGrabButton)) {
            sliderGrabButton = sliderGrabButton == ImGuiDir.Right ? ImGuiDir.Down : ImGuiDir.Right;
        }

        if (sliderGrabButton == ImGuiDir.Down) {
            ImGui.pushItemWidth(200);
            ImGui.colorPicker4("##CPSliderGrab", sliderGrabColor);
            ImGui.popItemWidth();
        }
    }
}

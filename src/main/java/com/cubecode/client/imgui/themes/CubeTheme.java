package com.cubecode.client.imgui.themes;

public class CubeTheme {
    public String name = "default";

    public CubeTheme(String name) {
        this.name = name;
    }

    public float alpha = 1f;
    public float disabledAlpha = 1f;
    public float[] windowPadding = new float[]{8, 8};
    public float windowRounding = 4f;
    public float windowBorderSize = 0f;
    public float[] windowMinSize = new float[]{32, 32};
    public float[] windowTitleAlign = new float[]{0.5f, 0.5f};
    public float childRounding = 4f;
    public float childBorderSize = 1f;
    public float popupRounding = 4f;
    public float popupBorderSize = 1f;
    public float[] framePadding = new float[]{4, 3};
    public float frameRounding = 4;
    public float frameBorderSize = 0;
    public float[] itemSpacing = new float[]{8, 4};
    public float[] itemInnerSpacing = new float[]{5, 4};
    public float indentSpacing = 21f;
    public float[] cellPadding = new float[]{5, 5};
    public float scrollbarSize = 13f;
    public float scrollbarRounding = 4f;
    public float grabMinSize = 8f;
    public float grabRounding = 4f;
    public float tabRounding = 4f;

    //COLORS
    public short[] text = new short[]{205, 214, 244, 255};
    public short[] textDisabled = new short[]{128, 128, 128, 255};
    public short[] windowBg = new short[]{30, 30, 46, 255};
    public short[] childBg = new short[]{1, 1, 1, 0};
    public short[] popupBg = new short[]{30, 30, 46, 255};
    public short[] border = new short[]{126, 126, 160, 128};
    public short[] borderShadow = new short[]{0, 0, 0, 0};
    public short[] frameBg = new short[]{23, 24, 35, 255};
    public short[] frameBgHovered = new short[]{39, 43, 56, 255};
    public short[] frameBgActive = new short[]{55, 61, 79, 255};
    public short[] titleBg = new short[]{24, 24, 37, 255};
    public short[] titleBgActive = new short[]{40, 42, 64, 255};
    public short[] titleBgCollapsed = new short[]{0, 0, 0, 130};
    public short[] menuBarBg = new short[]{17, 17, 27, 255};
    public short[] scrollbarBg = new short[]{17, 17, 27, 255};
    public short[] scrollbarGrab = new short[]{76, 79, 98, 255};
    public short[] scrollbarGrabHovered = new short[]{90, 93, 115, 255};
    public short[] scrollbarGrabActive = new short[]{90, 93, 120, 255};
    public short[] checkMark = new short[]{137, 220, 235, 255};
    public short[] sliderGrab = new short[]{137, 220, 235, 255};
    public short[] sliderGrabActive = new short[]{106, 232, 255, 255};
    public short[] button = new short[]{47, 46, 63, 255};
    public short[] buttonHovered = new short[]{75, 78, 96, 255};
    public short[] buttonActive = new short[]{35, 46, 63, 255};
    public short[] header = new short[]{47, 46, 63, 255};
    public short[] headerHovered = new short[]{75, 78, 96, 255};
    public short[] headerActive = new short[]{35, 34, 46, 255};
    public short[] separator = new short[]{60, 64, 99, 255};
    public short[] separatorHovered = new short[]{137, 180, 250, 255};
    public short[] separatorActive = new short[]{203, 166, 247, 255};
    public short[] resizeGrip = new short[]{137, 180, 250, 255};
    public short[] resizeGripHovered = new short[]{137, 180, 250, 255};
    public short[] resizeGripActive = new short[]{137, 180, 250, 255};
    public short[] tab = new short[]{88, 91, 112, 255};
    public short[] tabHovered = new short[]{99, 103, 128, 255};
    public short[] tabActive = new short[]{58, 60, 79, 255};
    public short[] tabUnfocused = new short[]{40, 45, 61, 255};
    public short[] tabUnfocusedActive = new short[]{30, 30, 46, 255};
    public short[] dockingPreview = new short[]{137, 220, 235, 255};
    public short[] dockingEmptyBg = new short[]{51, 51, 51, 255};
    public short[] plotLines = new short[]{156, 156, 156, 255};
    public short[] plotLinesHovered = new short[]{255, 110, 89, 255};
    public short[] plotHistogram = new short[]{230, 179, 0, 255};
    public short[] plotHistogramHovered = new short[]{255, 153, 0, 255};
    public short[] tableHeaderBg = new short[]{48, 48, 51, 255};
    public short[] tableBorderStrong = new short[]{79, 79, 89, 255};
    public short[] tableBorderLight = new short[]{59, 59, 64, 255};
    public short[] tableRowBg = new short[]{0, 0, 0, 0};
    public short[] tableRowBgAlt = new short[]{255, 255, 255, 15};
    public short[] textSelectedBg = new short[]{75, 80, 101, 255};
    public short[] dragDropTarget = new short[]{255, 255, 0, 255};
    public short[] navHighlight = new short[]{23, 24, 35, 255};
    public short[] navWindowingHighlight = new short[]{255, 255, 255, 179};
    public short[] navWindowingDimBg = new short[]{204, 204, 204, 51};
    public short[] modalWindowDimBg = new short[]{204, 204, 204, 89};
}

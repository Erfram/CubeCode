package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

/**
 * AbstractComponent serves as a base class for creating and manipulating UI components
 * in a graphical user interface framework. It provides a fluent interface for setting
 * various properties of the component, including position, size, colors, transparency,
 * padding, and rounding. This class allows for a high degree of customization and
 * flexibility when designing UI elements.
 */
public abstract class AbstractComponent {
    Float x = null;
    Float y = null;

    Float rx = null;
    Float supWidth = 0f;
    Float supWindowWidth = 0f;
    Float ry = null;
    Float supHeight = 0f;
    Float supWindowHeight = 0f;

    Float width = null;
    Float height = null;

    Float rw = null;
    Float rh = null;

    public int windowPosFlags = ImGuiCond.None;

    //STYLES
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

    public void render() {

    }

    /**
     * Sets the X coordinate of the component.
     */
    public AbstractComponent x(float x) {
        this.x = x;

        return this;
    }

    /**
     * Sets the X relative of the component.
     * If the Y relative (ry) is not set, it defaults to 0.
     */
    public AbstractComponent rx(float rx) {
        this.rx = rx;
        this.ry = this.ry == null ? 0 : this.ry;

        return this;
    }

    /**
     * Sets the Y coordinate of the component.
     */
    public AbstractComponent y(float y) {
        this.y = y;

        return this;
    }

    /**
     * Sets the Y relative of the component.
     * If the X relative (rx) is not set, it defaults to 0.
     */
    public AbstractComponent ry(float ry) {
        this.rx = this.rx == null ? 0 : this.rx;
        this.ry = ry;

        return this;
    }

    /**
     * Sets both the X and Y coordinates of the component.
     */
    public AbstractComponent xy(float x, float y) {
        this.x = x;
        this.y = y;

        return this;
    }

    /**
     * Sets both the X and Y relative of the component.
     */
    public AbstractComponent rxy(float rx, float ry) {
        this.rx = rx;
        this.ry = ry;

        return this;
    }

    /**
     * Resets the X coordinate of the component.
     */
    public AbstractComponent resetX() {
        this.x = null;

        return this;
    }

    /**
     * Resets the Y coordinate of the component.
     */
    public AbstractComponent resetY() {
        this.y = null;

        return this;
    }

    /**
     * Resets both the X and Y coordinates, as well as the relative X and relative Y.
     */
    public AbstractComponent resetPosition() {
        this.x = null;
        this.y = null;
        this.rx = null;
        this.ry = null;

        return this;
    }

    /**
     * Sets the width of the component.
     */
    public AbstractComponent w(float width) {
        this.width = width;
        return this;
    }

    /**
     * Resets the width of the component.
     */
    public AbstractComponent resetWidth() {
        this.width = null;
        return this;
    }

    /**
     * Sets the height of the component.
     */
    public AbstractComponent h(float height) {
        this.height = height;
        return this;
    }

    /**
     * Resets the height of the component.
     */
    public AbstractComponent resetHeight() {
        this.height = null;
        return this;
    }

    /**
     * Sets both the width and height of the component.
     */
    public AbstractComponent wh(float width, float height) {
        this.width = width;
        this.height = height;

        return this;
    }

    /**
     * Sets the relative width and height.
     */
    public AbstractComponent rwh(float rw, float rh) {
        this.rw = rw;
        this.rh = rh;
        return this;
    }

    /**
     * Sets the relative width of the component.
     */
    public AbstractComponent rw(float rw) {
        this.rw = rw;
        return this;
    }

    /**
     * Sets the relative height of the component.
     */
    public AbstractComponent rh(float rh) {
        this.rh = rh;
        return this;
    }

    /**
     * Resets the relative width and height of the component to null.
     */
    public AbstractComponent resetSize() {
        this.width = null;
        this.height = null;

        return this;
    }

    /**
     * Sets the color of the text.
     */
    public AbstractComponent colorText(short r, short g, short b, short a) {
        this.text[0] = r;
        this.text[1] = g;
        this.text[2] = b;
        this.text[3] = a;

        return this;
    }

    /**
     * Sets the color of the text in a disabled state.
     */
    public AbstractComponent colorTextDisabled(short r, short g, short b, short a) {
        this.textDisabled[0] = r;
        this.textDisabled[1] = g;
        this.textDisabled[2] = b;
        this.textDisabled[3] = a;
        return this;
    }

    /**
     * Sets the background color of the window.
     */
    public AbstractComponent colorWindowBg(short r, short g, short b, short a) {
        this.windowBg[0] = r;
        this.windowBg[1] = g;
        this.windowBg[2] = b;
        this.windowBg[3] = a;
        return this;
    }

    /**
     * Sets the background color of the child component.
     */
    public AbstractComponent colorChildBg(short r, short g, short b, short a) {
        this.childBg[0] = r;
        this.childBg[1] = g;
        this.childBg[2] = b;
        this.childBg[3] = a;
        return this;
    }

    /**
     * Sets the background color of the popup window.
     */
    public AbstractComponent colorPopupBg(short r, short g, short b, short a) {
        this.popupBg[0] = r;
        this.popupBg[1] = g;
        this.popupBg[2] = b;
        this.popupBg[3] = a;
        return this;
    }

    /**
     * Sets the border color.
     */
    public AbstractComponent colorBorder(short r, short g, short b, short a) {
        this.border[0] = r;
        this.border[1] = g;
        this.border[2] = b;
        this.border[3] = a;
        return this;
    }

    /**
     * Sets the color of the border shadow.
     */
    public AbstractComponent colorBorderShadow(short r, short g, short b, short a) {
        this.borderShadow[0] = r;
        this.borderShadow[1] = g;
        this.borderShadow[2] = b;
        this.borderShadow[3] = a;
        return this;
    }

    /**
     * Sets the background color of the frame.
     */
    public AbstractComponent colorFrameBg(short r, short g, short b, short a) {
        this.frameBg[0] = r;
        this.frameBg[1] = g;
        this.frameBg[2] = b;
        this.frameBg[3] = a;
        return this;
    }

    /**
     * Sets the background color of the frame when hovered.
     */
    public AbstractComponent colorFrameBgHovered(short r, short g, short b, short a) {
        this.frameBgHovered[0] = r;
        this.frameBgHovered[1] = g;
        this.frameBgHovered[2] = b;
        this.frameBgHovered[3] = a;
        return this;
    }

    /**
     * Sets the background color of the frame when it is active (e.g., being clicked or focused).
     */
    public AbstractComponent colorFrameBgActive(short r, short g, short b, short a) {
        this.frameBgActive[0] = r;
        this.frameBgActive[1] = g;
        this.frameBgActive[2] = b;
        this.frameBgActive[3] = a;
        return this;
    }

    /**
     * Sets the background color of the title bar.
     */
    public AbstractComponent colorTitleBg(short r, short g, short b, short a) {
        this.titleBg[0] = r;
        this.titleBg[1] = g;
        this.titleBg[2] = b;
        this.titleBg[3] = a;
        return this;
    }

    /**
     * Sets the background color of the title bar when it is active.
     */
    public AbstractComponent colorTitleBgActive(short r, short g, short b, short a) {
        this.titleBgActive[0] = r;
        this.titleBgActive[1] = g;
        this.titleBgActive[2] = b;
        this.titleBgActive[3] = a;
        return this;
    }

    /**
     * Sets the background color of the title bar when it is collapsed.
     */
    public AbstractComponent colorTitleBgCollapsed(short r, short g, short b, short a) {
        this.titleBgCollapsed[0] = r;
        this.titleBgCollapsed[1] = g;
        this.titleBgCollapsed[2] = b;
        this.titleBgCollapsed[3] = a;
        return this;
    }

    /**
     * Sets the background color of the menu bar.
     */
    public AbstractComponent colorMenuBarBg(short r, short g, short b, short a) {
        this.menuBarBg[0] = r;
        this.menuBarBg[1] = g;
        this.menuBarBg[2] = b;
        this.menuBarBg[3] = a;
        return this;
    }

    /**
     * Sets the background color of the scrollbar.
     */
    public AbstractComponent colorScrollbarBg(short r, short g, short b, short a) {
        this.scrollbarBg[0] = r;
        this.scrollbarBg[1] = g;
        this.scrollbarBg[2] = b;
        this.scrollbarBg[3] = a;
        return this;
    }

    /**
     * Sets the color of the scrollbar grab (the draggable part of the scrollbar).
     */
    public AbstractComponent colorScrollbarGrab(short r, short g, short b, short a) {
        this.scrollbarGrab[0] = r;
        this.scrollbarGrab[1] = g;
        this.scrollbarGrab[2] = b;
        this.scrollbarGrab[3] = a;
        return this;
    }

    /**
     * Sets the color of the scrollbar grab when it is hovered over.
     */
    public AbstractComponent colorScrollbarGrabHovered(short r, short g, short b, short a) {
        this.scrollbarGrabHovered[0] = r;
        this.scrollbarGrabHovered[1] = g;
        this.scrollbarGrabHovered[2] = b;
        this.scrollbarGrabHovered[3] = a;
        return this;
    }

    /**
     * Sets the color of the scrollbar grab when it is active (being dragged).
     */
    public AbstractComponent colorScrollbarGrabActive(short r, short g, short b, short a) {
        this.scrollbarGrabActive[0] = r;
        this.scrollbarGrabActive[1] = g;
        this.scrollbarGrabActive[2] = b;
        this.scrollbarGrabActive[3] = a;
        return this;
    }

    public AbstractComponent colorCheckMark(short r, short g, short b, short a) {
        this.checkMark[0] = r;
        this.checkMark[1] = g;
        this.checkMark[2] = b;
        this.checkMark[3] = a;
        return this;
    }

    public AbstractComponent colorSliderGrab(short r, short g, short b, short a) {
        this.sliderGrab[0] = r;
        this.sliderGrab[1] = g;
        this.sliderGrab[2] = b;
        this.sliderGrab[3] = a;
        return this;
    }

    public AbstractComponent colorSliderGrabActive(short r, short g, short b, short a) {
        this.sliderGrabActive[0] = r;
        this.sliderGrabActive[1] = g;
        this.sliderGrabActive[2] = b;
        this.sliderGrabActive[3] = a;
        return this;
    }

    public AbstractComponent colorButton(short r, short g, short b, short a) {
        this.button[0] = r;
        this.button[1] = g;
        this.button[2] = b;
        this.button[3] = a;
        return this;
    }

    public AbstractComponent colorButtonHovered(short r, short g, short b, short a) {
        this.buttonHovered[0] = r;
        this.buttonHovered[1] = g;
        this.buttonHovered[2] = b;
        this.buttonHovered[3] = a;
        return this;
    }

    public AbstractComponent colorButtonActive(short r, short g, short b, short a) {
        this.buttonActive[0] = r;
        this.buttonActive[1] = g;
        this.buttonActive[2] = b;
        this.buttonActive[3] = a;
        return this;
    }

    public AbstractComponent colorHeader(short r, short g, short b, short a) {
        this.header[0] = r;
        this.header[1] = g;
        this.header[2] = b;
        this.header[3] = a;
        return this;
    }

    public AbstractComponent colorHeaderHovered(short r, short g, short b, short a) {
        this.headerHovered[0] = r;
        this.headerHovered[1] = g;
        this.headerHovered[2] = b;
        this.headerHovered[3] = a;
        return this;
    }

    public AbstractComponent colorHeaderActive(short r, short g, short b, short a) {
        this.headerActive[0] = r;
        this.headerActive[1] = g;
        this.headerActive[2] = b;
        this.headerActive[3] = a;
        return this;
    }

    public AbstractComponent colorSeparator(short r, short g, short b, short a) {
        this.separator[0] = r;
        this.separator[1] = g;
        this.separator[2] = b;
        this.separator[3] = a;
        return this;
    }

    public AbstractComponent colorSeparatorHovered(short r, short g, short b, short a) {
        this.separatorHovered[0] = r;
        this.separatorHovered[1] = g;
        this.separatorHovered[2] = b;
        this.separatorHovered[3] = a;
        return this;
    }

    public AbstractComponent colorSeparatorActive(short r, short g, short b, short a) {
        this.separatorActive[0] = r;
        this.separatorActive[1] = g;
        this.separatorActive[2] = b;
        this.separatorActive[3] = a;
        return this;
    }

    public AbstractComponent colorResizeGrip(short r, short g, short b, short a) {
        this.resizeGrip[0] = r;
        this.resizeGrip[1] = g;
        this.resizeGrip[2] = b;
        this.resizeGrip[3] = a;
        return this;
    }

    public AbstractComponent colorResizeGripHovered(short r, short g, short b, short a) {
        this.resizeGripHovered[0] = r;
        this.resizeGripHovered[1] = g;
        this.resizeGripHovered[2] = b;
        this.resizeGripHovered[3] = a;
        return this;
    }

    public AbstractComponent colorResizeGripActive(short r, short g, short b, short a) {
        this.resizeGripActive[0] = r;
        this.resizeGripActive[1] = g;
        this.resizeGripActive[2] = b;
        this.resizeGripActive[3] = a;
        return this;
    }

    public AbstractComponent colorTab(short r, short g, short b, short a) {
        this.tab[0] = r;
        this.tab[1] = g;
        this.tab[2] = b;
        this.tab[3] = a;
        return this;
    }

    public AbstractComponent colorTabHovered(short r, short g, short b, short a) {
        this.tabHovered[0] = r;
        this.tabHovered[1] = g;
        this.tabHovered[2] = b;
        this.tabHovered[3] = a;
        return this;
    }

    public AbstractComponent colorTabActive(short r, short g, short b, short a) {
        this.tabActive[0] = r;
        this.tabActive[1] = g;
        this.tabActive[2] = b;
        this.tabActive[3] = a;
        return this;
    }

    public AbstractComponent colorTabUnfocused(short r, short g, short b, short a) {
        this.tabUnfocused[0] = r;
        this.tabUnfocused[1] = g;
        this.tabUnfocused[2] = b;
        this.tabUnfocused[3] = a;
        return this;
    }

    public AbstractComponent colorTabUnfocusedActive(short r, short g, short b, short a) {
        this.tabUnfocusedActive[0] = r;
        this.tabUnfocusedActive[1] = g;
        this.tabUnfocusedActive[2] = b;
        this.tabUnfocusedActive[3] = a;
        return this;
    }

    public AbstractComponent colorDockingPreview(short r, short g, short b, short a) {
        this.dockingPreview[0] = r;
        this.dockingPreview[1] = g;
        this.dockingPreview[2] = b;
        this.dockingPreview[3] = a;
        return this;
    }

    public AbstractComponent colorDockingEmptyBg(short r, short g, short b, short a) {
        this.dockingEmptyBg[0] = r;
        this.dockingEmptyBg[1] = g;
        this.dockingEmptyBg[2] = b;
        this.dockingEmptyBg[3] = a;
        return this;
    }

    public AbstractComponent colorPlotLines(short r, short g, short b, short a) {
        this.plotLines[0] = r;
        this.plotLines[1] = g;
        this.plotLines[2] = b;
        this.plotLines[3] = a;
        return this;
    }

    public AbstractComponent colorPlotLinesHovered(short r, short g, short b, short a) {
        this.plotLinesHovered[0] = r;
        this.plotLinesHovered[1] = g;
        this.plotLinesHovered[2] = b;
        this.plotLinesHovered[3] = a;
        return this;
    }

    public AbstractComponent colorPlotHistogram(short r, short g, short b, short a) {
        this.plotHistogram[0] = r;
        this.plotHistogram[1] = g;
        this.plotHistogram[2] = b;
        this.plotHistogram[3] = a;
        return this;
    }

    public AbstractComponent colorPlotHistogramHovered(short r, short g, short b, short a) {
        this.plotHistogramHovered[0] = r;
        this.plotHistogramHovered[1] = g;
        this.plotHistogramHovered[2] = b;
        this.plotHistogramHovered[3] = a;
        return this;
    }

    public AbstractComponent colorTableHeaderBg(short r, short g, short b, short a) {
        this.tableHeaderBg[0] = r;
        this.tableHeaderBg[1] = g;
        this.tableHeaderBg[2] = b;
        this.tableHeaderBg[3] = a;
        return this;
    }

    public AbstractComponent colorTableBorderStrong(short r, short g, short b, short a) {
        this.tableBorderStrong[0] = r;
        this.tableBorderStrong[1] = g;
        this.tableBorderStrong[2] = b;
        this.tableBorderStrong[3] = a;
        return this;
    }

    public AbstractComponent colorTableBorderLight(short r, short g, short b, short a) {
        this.tableBorderLight[0] = r;
        this.tableBorderLight[1] = g;
        this.tableBorderLight[2] = b;
        this.tableBorderLight[3] = a;
        return this;
    }

    public AbstractComponent colorTableRowBg(short r, short g, short b, short a) {
        this.tableRowBg[0] = r;
        this.tableRowBg[1] = g;
        this.tableRowBg[2] = b;
        this.tableRowBg[3] = a;
        return this;
    }

    public AbstractComponent colorTableRowBgAlt(short r, short g, short b, short a) {
        this.tableRowBgAlt[0] = r;
        this.tableRowBgAlt[1] = g;
        this.tableRowBgAlt[2] = b;
        this.tableRowBgAlt[3] = a;
        return this;
    }

    public AbstractComponent colorTextSelectedBg(short r, short g, short b, short a) {
        this.textSelectedBg[0] = r;
        this.textSelectedBg[1] = g;
        this.textSelectedBg[2] = b;
        this.textSelectedBg[3] = a;
        return this;
    }

    public AbstractComponent colorDragDropTarget(short r, short g, short b, short a) {
        this.dragDropTarget[0] = r;
        this.dragDropTarget[1] = g;
        this.dragDropTarget[2] = b;
        this.dragDropTarget[3] = a;
        return this;
    }

    public AbstractComponent colorNavHighlight(short r, short g, short b, short a) {
        this.navHighlight[0] = r;
        this.navHighlight[1] = g;
        this.navHighlight[2] = b;
        this.navHighlight[3] = a;
        return this;
    }

    public AbstractComponent colorNavWindowingHighlight(short r, short g, short b, short a) {
        this.navWindowingHighlight[0] = r;
        this.navWindowingHighlight[1] = g;
        this.navWindowingHighlight[2] = b;
        this.navWindowingHighlight[3] = a;
        return this;
    }

    public AbstractComponent colorNavWindowingDimBg(short r, short g, short b, short a) {
        this.navWindowingDimBg[0] = r;
        this.navWindowingDimBg[1] = g;
        this.navWindowingDimBg[2] = b;
        this.navWindowingDimBg[3] = a;
        return this;
    }

    public AbstractComponent colorModalWindowDimBg(short r, short g, short b, short a) {
        this.modalWindowDimBg[0] = r;
        this.modalWindowDimBg[1] = g;
        this.modalWindowDimBg[2] = b;
        this.modalWindowDimBg[3] = a;
        return this;
    }

    /////////////

    public AbstractComponent alpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public AbstractComponent disabledAlpha(float disabledAlpha) {
        this.disabledAlpha = disabledAlpha;
        return this;
    }

    public AbstractComponent windowPadding(float x, float y) {
        this.windowPadding[0] = x;
        this.windowPadding[1] = y;

        return this;
    }

    public AbstractComponent windowRounding(float windowRounding) {
        this.windowRounding = windowRounding;

        return this;
    }

    public AbstractComponent windowBorderSize(float windowBorderSize) {
        this.windowBorderSize = windowBorderSize;

        return this;
    }

    public AbstractComponent windowMinSize(float x, float y) {
        this.windowMinSize[0] = x;
        this.windowMinSize[1] = y;

        return this;
    }

    public AbstractComponent windowTitleAlign(float x, float y) {
        this.windowTitleAlign[0] = x;
        this.windowTitleAlign[1] = y;

        return this;
    }

    public AbstractComponent childRounding(float childRounding) {
        this.childRounding = childRounding;

        return this;
    }

    public AbstractComponent childBorderSize(float childBorderSize) {
        this.childBorderSize = childBorderSize;

        return this;
    }

    public AbstractComponent popupRounding(float popupRounding) {
        this.popupRounding = popupRounding;

        return this;
    }

    public AbstractComponent popupBorderSize(float popupBorderSize) {
        this.popupBorderSize = popupBorderSize;

        return this;
    }

    public AbstractComponent framePadding(float x, float y) {
        this.framePadding[0] = x;
        this.framePadding[1] = y;

        return this;
    }

    public AbstractComponent frameRounding(float frameRounding) {
        this.frameRounding = frameRounding;

        return this;
    }

    public AbstractComponent frameBorderSize(float frameBorderSize) {
        this.frameBorderSize = frameBorderSize;

        return this;
    }

    public AbstractComponent itemSpacing(float x, float y) {
        this.itemSpacing[0] = x;
        this.itemSpacing[1] = y;

        return this;
    }

    public AbstractComponent itemInnerSpacing(float x, float y) {
        this.itemInnerSpacing[0] = x;
        this.itemInnerSpacing[1] = y;

        return this;
    }

    public AbstractComponent indentSpacing(float indentSpacing) {
        this.indentSpacing = indentSpacing;

        return this;
    }

    public AbstractComponent cellPadding(float x, float y) {
        this.cellPadding[0] = x;
        this.cellPadding[1] = y;

        return this;
    }

    public AbstractComponent scrollbarSize(float scrollbarSize) {
        this.scrollbarSize = scrollbarSize;

        return this;
    }

    public AbstractComponent scrollbarRounding(float scrollbarRounding) {
        this.scrollbarRounding = scrollbarRounding;

        return this;
    }

    public AbstractComponent grabMinSize(float grabMinSize) {
        this.grabMinSize = grabMinSize;

        return this;
    }

    public AbstractComponent grabRounding(float grabRounding) {
        this.grabRounding = grabRounding;

        return this;
    }

    public AbstractComponent tabRounding(float tabRounding) {
        this.tabRounding = tabRounding;

        return this;
    }

    void pushPosition() {
        if (!(this instanceof WindowComponent)) {
            if (this.rx != null) {
                ImGui.setCursorPosX(ImGui.getWindowSizeX() * this.rx - this.supWidth / 2);
            } else if (this.x != null) {
                ImGui.setCursorPosX(this.x);
            }

            if (this.ry != null) {
                ImGui.setCursorPosY(ImGui.getWindowSizeY() * this.ry - this.supHeight / 2);
            } else if (this.y != null) {
                ImGui.setCursorPosY(this.y);
            }
        } else {
            ImVec2 center = ImGui.getMainViewport().getCenter();

            float x = this.x == null ? 0 : this.x;
            float y = this.y == null ? 0 : this.y;

            if (this.rx != null && this.ry != null) {
                ImGui.setNextWindowPos(center.x, center.y, this.windowPosFlags, this.rx, this.ry);
            } else {
                ImGui.setNextWindowPos(x, y, this.windowPosFlags);
            }
        }
    }

    void popPosition() {
        if (!(this instanceof WindowComponent)) {
            this.supWidth = ImGui.getItemRectSizeX();
            this.supHeight = ImGui.getItemRectSizeY();
        }
    }

    void pushSize() {
        if (!(this instanceof WindowComponent)) {
            if (this.rw != null) {
                ImGui.pushItemWidth(ImGui.getWindowSizeX() * this.rw);
            } else if (this.width != null) {
                ImGui.pushItemWidth(this.width);
            }
        } else {
            Window window = MinecraftClient.getInstance().getWindow();

            float width = this.rw != null ? window.getWidth() * this.rw : this.width != null ? this.width : 0;
            float height = this.rh != null ? window.getHeight() * this.rh : this.height != null ? this.height : 0;

            ImGui.setNextWindowSize(width, height, this.windowPosFlags);

            this.supWindowWidth = width;
            this.supWindowHeight = height;
        }
    }

    void popSize() {
        if (!(this instanceof WindowComponent)) {
            if (this.width != null) {
                ImGui.popItemWidth();
            }
        }
    }

    void pushTheme() {
        this.pushStyles();
        this.pushColors();
    }

    void popTheme() {
        this.popStyles();
        this.popColors();
    }

    void pushColors() {
        ImGui.pushStyleColor(ImGuiCol.Text, this.text[0], this.text[1], this.text[2], this.text[3]);
        ImGui.pushStyleColor(ImGuiCol.TextDisabled, this.textDisabled[0], this.textDisabled[1], this.textDisabled[2], this.textDisabled[3]);
        ImGui.pushStyleColor(ImGuiCol.WindowBg, this.windowBg[0], this.windowBg[1], this.windowBg[2], this.windowBg[3]);
        ImGui.pushStyleColor(ImGuiCol.ChildBg, this.childBg[0], this.childBg[1], this.childBg[2], this.childBg[3]);
        ImGui.pushStyleColor(ImGuiCol.PopupBg, this.popupBg[0], this.popupBg[1], this.popupBg[2], this.popupBg[3]);
        ImGui.pushStyleColor(ImGuiCol.Border, this.border[0], this.border[1], this.border[2], this.border[3]);
        ImGui.pushStyleColor(ImGuiCol.BorderShadow, this.borderShadow[0], this.borderShadow[1], this.borderShadow[2], this.borderShadow[3]);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, this.frameBg[0], this.frameBg[1], this.frameBg[2], this.frameBg[3]);
        ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, this.frameBgHovered[0], this.frameBgHovered[1], this.frameBgHovered[2], this.frameBgHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.FrameBgActive, this.frameBgActive[0], this.frameBgActive[1], this.frameBgActive[2], this.frameBgActive[3]);
        ImGui.pushStyleColor(ImGuiCol.TitleBg, this.titleBg[0], this.titleBg[1], this.titleBg[2], this.titleBg[3]);
        ImGui.pushStyleColor(ImGuiCol.TitleBgActive, this.titleBgActive[0], this.titleBgActive[1], this.titleBgActive[2], this.titleBgActive[3]);
        ImGui.pushStyleColor(ImGuiCol.TitleBgCollapsed, this.titleBgCollapsed[0], this.titleBgCollapsed[1], this.titleBgCollapsed[2], this.titleBgCollapsed[3]);
        ImGui.pushStyleColor(ImGuiCol.MenuBarBg, this.menuBarBg[0], this.menuBarBg[1], this.menuBarBg[2], this.menuBarBg[3]);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, this.scrollbarBg[0], this.scrollbarBg[1], this.scrollbarBg[2], this.scrollbarBg[3]);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, this.scrollbarGrab[0], this.scrollbarGrab[1], this.scrollbarGrab[2], this.scrollbarGrab[3]);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, this.scrollbarGrabHovered[0], this.scrollbarGrabHovered[1], this.scrollbarGrabHovered[2], this.scrollbarGrabHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, this.scrollbarGrabActive[0], this.scrollbarGrabActive[1], this.scrollbarGrabActive[2], this.scrollbarGrabActive[3]);
        ImGui.pushStyleColor(ImGuiCol.CheckMark, this.checkMark[0], this.checkMark[1], this.checkMark[2], this.checkMark[3]);
        ImGui.pushStyleColor(ImGuiCol.SliderGrab, this.sliderGrab[0], this.sliderGrab[1], this.sliderGrab[2], this.sliderGrab[3]);
        ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, this.sliderGrabActive[0], this.sliderGrabActive[1], this.sliderGrabActive[2], this.sliderGrabActive[3]);
        ImGui.pushStyleColor(ImGuiCol.Button, this.button[0], this.button[1], this.button[2], this.button[3]);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, this.buttonHovered[0], this.buttonHovered[1], this.buttonHovered[2], this.buttonHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, this.buttonActive[0], this.buttonActive[1], this.buttonActive[2], this.buttonActive[3]);
        ImGui.pushStyleColor(ImGuiCol.Header, this.header[0], this.header[1], this.header[2], this.header[3]);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, this.headerHovered[0], this.headerHovered[1], this.headerHovered[2], this.headerHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, this.headerActive[0], this.headerActive[1], this.headerActive[2], this.headerActive[3]);
        ImGui.pushStyleColor(ImGuiCol.Separator, this.separator[0], this.separator[1], this.separator[2], this.separator[3]);
        ImGui.pushStyleColor(ImGuiCol.SeparatorHovered, this.separatorHovered[0], this.separatorHovered[1], this.separatorHovered[2], this.separatorHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.SeparatorActive, this.separatorActive[0], this.separatorActive[1], this.separatorActive[2], this.separatorActive[3]);
        ImGui.pushStyleColor(ImGuiCol.ResizeGrip, this.resizeGrip[0], this.resizeGrip[1], this.resizeGrip[2], this.resizeGrip[3]);
        ImGui.pushStyleColor(ImGuiCol.ResizeGripHovered, this.resizeGripHovered[0], this.resizeGripHovered[1], this.resizeGripHovered[2], this.resizeGripHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.ResizeGripActive, this.resizeGripActive[0], this.resizeGripActive[1], this.resizeGripActive[2], this.resizeGripActive[3]);
        ImGui.pushStyleColor(ImGuiCol.Tab, this.tab[0], this.tab[1], this.tab[2], this.tab[3]);
        ImGui.pushStyleColor(ImGuiCol.TabHovered, this.tabHovered[0], this.tabHovered[1], this.tabHovered[2], this.tabHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.TabActive, this.tabActive[0], this.tabActive[1], this.tabActive[2], this.tabActive[3]);
        ImGui.pushStyleColor(ImGuiCol.TabUnfocused, this.tabUnfocused[0], this.tabUnfocused[1], this.tabUnfocused[2], this.tabUnfocused[3]);
        ImGui.pushStyleColor(ImGuiCol.TabUnfocusedActive, this.tabUnfocusedActive[0], this.tabUnfocusedActive[1], this.tabUnfocusedActive[2], this.tabUnfocusedActive[3]);
        ImGui.pushStyleColor(ImGuiCol.DockingPreview, this.dockingPreview[0], this.dockingPreview[1], this.dockingPreview[2], this.dockingPreview[3]);
        ImGui.pushStyleColor(ImGuiCol.DockingEmptyBg, this.dockingEmptyBg[0], this.dockingEmptyBg[1], this.dockingEmptyBg[2], this.dockingEmptyBg[3]);
        ImGui.pushStyleColor(ImGuiCol.PlotLines, this.plotLines[0], this.plotLines[1], this.plotLines[2], this.plotLines[3]);
        ImGui.pushStyleColor(ImGuiCol.PlotLinesHovered, this.plotLinesHovered[0], this.plotLinesHovered[1], this.plotLinesHovered[2], this.plotLinesHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.PlotHistogram, this.plotHistogram[0], this.plotHistogram[1], this.plotHistogram[2], this.plotHistogram[3]);
        ImGui.pushStyleColor(ImGuiCol.PlotHistogramHovered, this.plotHistogramHovered[0], this.plotHistogramHovered[1], this.plotHistogramHovered[2], this.plotHistogramHovered[3]);
        ImGui.pushStyleColor(ImGuiCol.TableHeaderBg, this.tableHeaderBg[0], this.tableHeaderBg[1], this.tableHeaderBg[2], this.tableHeaderBg[3]);
        ImGui.pushStyleColor(ImGuiCol.TableBorderStrong, this.tableBorderStrong[0], this.tableBorderStrong[1], this.tableBorderStrong[2], this.tableBorderStrong[3]);
        ImGui.pushStyleColor(ImGuiCol.TableBorderLight, this.tableBorderLight[0], this.tableBorderLight[1], this.tableBorderLight[2], this.tableBorderLight[3]);
        ImGui.pushStyleColor(ImGuiCol.TableRowBg, this.tableRowBg[0], this.tableRowBg[1], this.tableRowBg[2], this.tableRowBg[3]);
        ImGui.pushStyleColor(ImGuiCol.TableRowBgAlt, this.tableRowBgAlt[0], this.tableRowBgAlt[1], this.tableRowBgAlt[2], this.tableRowBgAlt[3]);
        ImGui.pushStyleColor(ImGuiCol.TextSelectedBg, this.textSelectedBg[0], this.textSelectedBg[1], this.textSelectedBg[2], this.textSelectedBg[3]);
        ImGui.pushStyleColor(ImGuiCol.DragDropTarget, this.dragDropTarget[0], this.dragDropTarget[1], this.dragDropTarget[2], this.dragDropTarget[3]);
        ImGui.pushStyleColor(ImGuiCol.NavHighlight, this.navHighlight[0], this.navHighlight[1], this.navHighlight[2], this.navHighlight[3]);
        ImGui.pushStyleColor(ImGuiCol.NavWindowingHighlight, this.navWindowingHighlight[0], this.navWindowingHighlight[1], this.navWindowingHighlight[2], this.navWindowingHighlight[3]);
        ImGui.pushStyleColor(ImGuiCol.NavWindowingDimBg, this.navWindowingDimBg[0], this.navWindowingDimBg[1], this.navWindowingDimBg[2], this.navWindowingDimBg[3]);
        ImGui.pushStyleColor(ImGuiCol.ModalWindowDimBg, this.modalWindowDimBg[0], this.modalWindowDimBg[1], this.modalWindowDimBg[2], this.modalWindowDimBg[3]);
    }

    void popColors() {
        ImGui.popStyleColor(55);
    }

    void pushStyles() {
        ImGui.pushStyleVar(ImGuiStyleVar.Alpha, this.alpha);
        ImGui.pushStyleVar(ImGuiStyleVar.DisabledAlpha, this.disabledAlpha);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, this.windowPadding[0], this.windowPadding[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, this.windowRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, this.windowBorderSize);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowMinSize, this.windowMinSize[0], this.windowMinSize[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowTitleAlign, this.windowTitleAlign[0], this.windowTitleAlign[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, this.childRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildBorderSize, this.childBorderSize);
        ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, this.popupRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.PopupBorderSize, this.popupBorderSize);
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, this.framePadding[0], this.framePadding[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, this.frameRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, this.frameBorderSize);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, this.itemSpacing[0], this.itemSpacing[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, this.itemInnerSpacing[0], this.itemInnerSpacing[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.IndentSpacing, this.indentSpacing);
        ImGui.pushStyleVar(ImGuiStyleVar.CellPadding, this.cellPadding[0], this.cellPadding[1]);
        ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarSize, this.scrollbarSize);
        ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, this.scrollbarRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.GrabMinSize, this.grabMinSize);
        ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, this.grabRounding);
        ImGui.pushStyleVar(ImGuiStyleVar.TabRounding, this.tabRounding);
    }

    void popStyles() {
        ImGui.popStyleVar(23);
    }

    public Float getWidth() {
        return this.width != null ? this.width : this.rw != null ? MinecraftClient.getInstance().getWindow().getWidth() * this.rw : 0;
    }

    public Float getHeight() {
        return this.height != null ? this.height : this.rh != null ? MinecraftClient.getInstance().getWindow().getHeight() * this.rh : 0;
    }

    public Float getX() {
        return this.x != null ? this.x : this.rx != null ? MinecraftClient.getInstance().getWindow().getWidth() * this.rx : 0;
    }

    public Float getY() {
        return this.y != null ? this.y : this.ry != null ? MinecraftClient.getInstance().getWindow().getHeight() * this.ry : 0;
    }

    public Float getRelativeWidth() {
        return this.rw != null ? this.rw : 0;
    }

    public Float getRelativeHeight() {
        return this.rh != null ? this.rh : 0;
    }

    public Float getRelativeX() {
        return this.rx != null ? this.rx : 0;
    }

    public Float getRelativeY() {
        return this.ry != null ? this.ry : 0;
    }
}
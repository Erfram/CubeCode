package com.cubecode.client.scripts.code.ui.components.draw;

import com.cubecode.client.scripts.code.ui.components.AbstractComponent;
import com.cubecode.utils.ColorUtils;
import imgui.ImDrawList;
import imgui.ImGui;

public class DrawComponent extends AbstractComponent {
    ImDrawList drawList;
    Runnable onRender;

    public DrawComponent() {
        this.drawList = ImGui.getWindowDrawList();
    }

    /**
     *
     */
    public void rect(float minX, float minY, float maxX, float maxY) {
        this.onRender = () -> this.drawList.addRect(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1)
        );
    }

    public void rect(float minX, float minY, float maxX, float maxY, float rounding) {
        this.onRender = () -> this.drawList.addRect(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1),
                rounding
        );
    }

    public void rect(float minX, float minY, float maxX, float maxY, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addRect(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255)
        );
    }

    public void rect(float minX, float minY, float maxX, float maxY, int r, int g, int b, int a, float rounding) {
        this.onRender = () -> this.drawList.addRect(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255),
                rounding
        );
    }

    public void rectFilled(float minX, float minY, float maxX, float maxY) {
        this.onRender = () -> this.drawList.addRectFilled(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1)
        );
    }

    public void rectFilled(float minX, float minY, float maxX, float maxY, float rounding) {
        this.onRender = () -> this.drawList.addRectFilled(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1),
                rounding
        );
    }

    public void rectFilled(float minX, float minY, float maxX, float maxY, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addRectFilled(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255)
        );
    }

    public void rectFilled(float minX, float minY, float maxX, float maxY, int r, int g, int b, int a, float rounding) {
        this.onRender = () -> this.drawList.addRectFilled(
                ImGui.getWindowPosX() + minX, ImGui.getWindowPosY() + minY,
                ImGui.getWindowPosX() + maxX, ImGui.getWindowPosY() + maxY,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255),
                rounding
        );
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        this.quad(x1, y1, x2, y2, x3, y3, x4, y4, 0, 0, 0, 255);
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addQuad(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.getWindowPosX() + x3, ImGui.getWindowPosY() + y3,
                ImGui.getWindowPosX() + x4, ImGui.getWindowPosY() + y4,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255)
        );
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float thickness) {
        this.onRender = () -> this.drawList.addQuad(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.getWindowPosX() + x3, ImGui.getWindowPosY() + y3,
                ImGui.getWindowPosX() + x4, ImGui.getWindowPosY() + y4,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1),
                thickness
        );
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, float thickness, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addQuad(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.getWindowPosX() + x3, ImGui.getWindowPosY() + y3,
                ImGui.getWindowPosX() + x4, ImGui.getWindowPosY() + y4,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255),
                thickness
        );
    }

    public void quadFilled(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        this.onRender = () -> this.drawList.addQuad(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.getWindowPosX() + x3, ImGui.getWindowPosY() + y3,
                ImGui.getWindowPosX() + x4, ImGui.getWindowPosY() + y4,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1)
        );
    }

    public void quadFilled(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addQuad(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.getWindowPosX() + x3, ImGui.getWindowPosY() + y3,
                ImGui.getWindowPosX() + x4, ImGui.getWindowPosY() + y4,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255)
        );
    }

    public void line(float x1, float y1, float x2, float y2, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addLine(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255)
        );
    }

    public void line(float x1, float y1, float x2, float y2) {
        this.onRender = () -> this.drawList.addLine(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1)
        );
    }

    public void line(float x1, float y1, float x2, float y2, int r, int g, int b, int a, float thickness) {
        this.onRender = () -> this.drawList.addLine(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255),
                thickness
        );
    }

    public void line(float x1, float y1, float x2, float y2, float thickness) {
        this.onRender = () -> this.drawList.addLine(
                ImGui.getWindowPosX() + x1, ImGui.getWindowPosY() + y1,
                ImGui.getWindowPosX() + x2, ImGui.getWindowPosY() + y2,
                ImGui.colorConvertFloat4ToU32(0, 0, 0, 1),
                thickness
        );
    }

    public void circle(float centerX, float centerY, float radius, int r, int g, int b, int a) {
        this.onRender = () -> this.drawList.addCircle(
                ImGui.getWindowPosX() + centerX,
                ImGui.getWindowPosY() + centerY,
                radius,
                ImGui.colorConvertFloat4ToU32(r/255, g/255, b/255, a/255)
        );
    }

    @Override
    public void render() {
        this.onRender.run();
    }
}

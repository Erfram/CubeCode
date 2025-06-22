package com.cubecode.client.imgui;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiMouseCursor;

import java.util.function.Consumer;

public class CubeImGui {
    View view;

    public CubeImGui(View view) {
        this.view = view;
    }

    public static void mainMenuBar(Runnable runnable) {
        ImGui.beginMainMenuBar();
        runnable.run();
        ImGui.endMainMenuBar();
    }

    public static void menu(String label, Runnable runnable) {
        if (ImGui.beginMenu(label)) {
            runnable.run();
            ImGui.endMenu();
        }
    }

    public static void menuItem(String label, Runnable runnable) {
        if (ImGui.menuItem(label)) {
            runnable.run();
        }
    }

    public static void child(String id, Runnable runnable) {
        if (ImGui.beginChild(id)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public static void child(String id, float width, float height, Runnable runnable) {
        if (ImGui.beginChild(id, width, height)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public static void child(String id, float width, float height, boolean border, Runnable runnable) {
        if (ImGui.beginChild(id, width, height, border)) {
            runnable.run();
        }

        ImGui.endChild();
    }

    public void dragFloat(String label, float defaultValue) {
        this.view.putVariable(label, new float[]{defaultValue});
        ImGui.dragFloat(label, this.view.getVariable(label), 0.1f, 0.0f, 1.0f, "%.2f");
    }

    public void dragInt(String label, int defaultValue) {
        this.view.putVariable(label, new int[]{defaultValue});
        ImGui.dragInt(label, this.view.getVariable(label), 1, 1, 4);
    }

    public void sliderInt(String label, int defaultValue) {
        this.view.putVariable(label, new int[]{defaultValue});
        ImGui.sliderInt(label, this.view.getVariable(label), 1, 4);
    }

    public void sliderInt(String label, int defaultValue, Consumer<Integer> callback) {
        this.view.putVariable(label, new int[]{defaultValue});
        int[] variable = this.view.getVariable(label);
        if (ImGui.sliderInt(label, variable, 1, 4)) {
            callback.accept(variable[0]);
        }
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * cig.splitter("splitter", 4, availableWidth, ImGui.getItemRectMaxY(), 0.3f, 0.1f, 0.9f)
     * }</pre>
     */
    public void splitter(String splitterId, float splitterWidth, float availableWidth, float availableHeight, float defaultRatio, float minSplitRatio, float maxSplitRatio) {
        this.view.putVariable(splitterId, defaultRatio);

        ImGui.invisibleButton(splitterId, splitterWidth, availableHeight);

        ImGui.getWindowDrawList().addRect(
                ImGui.getItemRectMin().x, ImGui.getItemRectMin().y,
                ImGui.getItemRectMax().x, availableHeight,
                ImGui.colorConvertFloat4ToU32(1, 1, 1, 1)
        );

        if (ImGui.isItemActive()) {
            float mouseDeltaX = ImGui.getIO().getMouseDeltaX();
            float newSplitRatio = ((float) this.view.getVariable(splitterId)) + mouseDeltaX / availableWidth;

            if (newSplitRatio >= minSplitRatio && newSplitRatio <= maxSplitRatio) {
                this.view.setVariable(splitterId, newSplitRatio);
            }
        }

        if (ImGui.isItemHovered()) {
            ImGui.setMouseCursor(ImGuiMouseCursor.ResizeEW);
        }
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * cig.splitter("splitter", 4, availableWidth, ImGui.getItemRectMaxY())
     * }</pre>
     */
    public void splitter(String splitterId, float splitterWidth, float availableWidth, float availableHeight) {
        this.splitter(splitterId, splitterWidth, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }

    /**
     * Example:
     * <pre>{@code
     * float availableWidth = ImGui.getWindowSize().x - ImGui.getStyle().getWindowPaddingX();
     *
     * CubeImGui.beginChild("child", availableWidth * ((float)this.getVariable("splitter")), 0, true, () -> {});
     *
     * cig.splitter("splitter", availableWidth, ImGui.getItemRectMaxY())
     * }</pre>
     */
    public void splitter(String splitterId, float availableWidth, float availableHeight) {
        this.splitter(splitterId, 4, availableWidth, availableHeight, 0.3f, 0.1f, 0.9f);
    }
}

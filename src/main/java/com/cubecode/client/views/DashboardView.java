package com.cubecode.client.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.views.factory.BlockView;
import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;

public class DashboardView extends View {
    @Override
    public void render() {
        CubeImGui.mainMenuBar(() -> {
            CubeImGui.menu("Windows", () -> {
                CubeImGui.menuItem("CodeEditor", () -> {
                    ImGuiLoader.pushView(new CodeEditorView());
                });

                CubeImGui.menuItem("Test", () -> {
                    ImGuiLoader.pushView(new TestView());
                });
            });

            CubeImGui.menu("Factory", () -> {
                CubeImGui.menuItem("Block", () -> {
                   ImGuiLoader.pushView(new BlockView());
                });
            });
        });

        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }

    @Override
    public void handleKeyReleased(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void handleKeyPressed(int keyCode, int scanCode, int modifiers) {

    }

    @Override
    public void handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {

    }

    @Override
    public void handleMouseClicked(double mouseX, double mouseY, int button) {

    }

    @Override
    public void handleMouseReleased(double mouseX, double mouseY, int button) {

    }
}

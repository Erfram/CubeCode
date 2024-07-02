package com.cubecode.client.views;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;

public class DashboardView extends View {

    @Override
    public void render() {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Windows")) {
                if (ImGui.menuItem("CodeEditor")) {
                    ImGuiLoader.pushView(new CodeEditorView());
                }
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
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

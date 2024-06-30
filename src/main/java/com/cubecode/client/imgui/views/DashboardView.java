package com.cubecode.client.imgui.views;

import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;
import com.cubecode.client.imgui.basic.View;

public class DashboardView extends View {
    @Override
    public void render() {
        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("Windows")) {
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }
}

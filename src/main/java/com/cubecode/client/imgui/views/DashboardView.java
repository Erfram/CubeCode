package com.cubecode.client.imgui.views;

import com.cubecode.client.editor.CodeEditor;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import imgui.ImGui;
import imgui.flag.ImGuiDockNodeFlags;
import com.cubecode.client.imgui.basic.View;

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
}

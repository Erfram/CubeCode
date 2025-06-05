package com.cubecode.client.imgui;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;

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

    public void dragFloat(String label, float defaultValue) {
        this.view.putVariable(label, new float[]{defaultValue});
        ImGui.dragFloat(label, this.view.getVariable(label), 0.1f, 0.0f, 1.0f, "%.2f");
    }

    public void dragInt(String label, int defaultValue) {
        this.view.putVariable(label, new int[]{defaultValue});
        ImGui.dragInt(label, this.view.getVariable(label), 1, 0, 4);
    }

    public void sliderInt(String label, int defaultValue) {
        this.view.putVariable(label, new int[]{defaultValue});
        ImGui.sliderInt(label, this.view.getVariable(label), 0, 4);
    }
}

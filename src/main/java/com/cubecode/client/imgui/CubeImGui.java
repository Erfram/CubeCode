package com.cubecode.client.imgui;

import imgui.ImGui;

public class CubeImGui {
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
}

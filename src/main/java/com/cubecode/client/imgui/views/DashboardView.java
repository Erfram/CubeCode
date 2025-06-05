package com.cubecode.client.imgui.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.views.ide.IDEView;

public class DashboardView extends View {
    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void render() {
        CubeImGui.mainMenuBar(() -> {
            CubeImGui.menu("Windows", () -> {
                CubeImGui.menuItem("IDE", () -> {
                    ImGuiLoader.pushView(new IDEView());
                });

                CubeImGui.menuItem("Settings", () -> {
                    ImGuiLoader.pushView(new SettingsView());
                });
            });
        });
    }
}

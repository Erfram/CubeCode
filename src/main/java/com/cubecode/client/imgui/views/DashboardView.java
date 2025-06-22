package com.cubecode.client.imgui.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.IDERequestedPacket;

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
                    Dispatcher.sendToServer(new IDERequestedPacket());
                });

                CubeImGui.menuItem("Settings", () -> {
                    ImGuiLoader.pushView(new SettingsView());
                });
            });
        });
    }
}

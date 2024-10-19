package com.cubecode.client.views;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.basic.WindowData;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDEARequestedPacket;
import com.cubecode.utils.Icons;
import imgui.*;
import imgui.flag.ImGuiDockNodeFlags;
import net.minecraft.text.Text;

public class DashboardView extends View {
    @Override
    public void init() {
        CubeCodeClient.windowStateManager.loadWindowState();
        CubeCodeClient.windowStateManager.loadSessionWindowState();
    }

    @Override
    public void render() {
        CubeImGui.mainMenuBar(() -> {
            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.windows.title").getString(), () -> {
                CubeImGui.menuItem("CubeCodeIDEA", () -> {
                    Dispatcher.sendToServer(new IDEARequestedPacket());
                });

                CubeImGui.menuItem(Text.translatable("imgui.cubecode.windows.events.title").getString(), () -> {
                    Dispatcher.sendToServer(new EventsRequestedPacket());
                });
            });

            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.settings.title").getString(), () -> {
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.dashboard.settings.title").getString(), () -> ImGuiLoader.pushView(new SettingsView()));
            });

            float windowWidth = ImGui.getWindowWidth();
            float menuBarHeight = ImGui.getFrameHeight();

            float iconSize = menuBarHeight - 4;
            float iconPosX = windowWidth - iconSize - 10;

            ImGui.setCursorPosX(iconPosX);

            CubeImGui.imageButton(Icons.SAVE, Text.translatable("imgui.cubecode.dashboard.saveWindows.title").getString(), 16, 16, () -> {
                CubeCodeClient.windowStateManager.saveWindowState();
            });
        });

        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }

    @Override
    public void onClose() {
        for (View view : ImGuiLoader.getViews()) {
            if (view instanceof DashboardView)
                continue;

            CubeCodeClient.windowStateManager.addSessionWindow(view, new WindowData(
                    view.windowPos.x,
                    view.windowPos.y,
                    view.windowSize.x,
                    view.windowSize.y,
                    view.windowCollapsed
            ));
        }
    }
}

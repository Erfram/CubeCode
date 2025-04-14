package com.cubecode.client.views;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.basic.ViewDataManager;
import com.cubecode.client.views.ide.core.CubeCodeIDEView2;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDERequestedPacket;
import com.cubecode.network.packets.all.StatesRequestedPacket;
import com.cubecode.utils.Icons;
import imgui.*;
import imgui.flag.ImGuiDockNodeFlags;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class DashboardView extends View {
    @Override
    public void init() {
        CubeCodeClient.viewDataManager.loadSessionViews();
    }

    @Override
    public void render() {
        CubeImGui.mainMenuBar(() -> {
            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.windows.title").getString(), () -> {
                CubeImGui.menuItem("CubeCodeIDE", () -> {
                    Dispatcher.sendToServer(new IDERequestedPacket());
                });

                CubeImGui.menuItem("CubeCodeIDE2", () -> {
                    ImGuiLoader.pushView(new CubeCodeIDEView2());
                });

                CubeImGui.menuItem(Text.translatable("imgui.cubecode.windows.events.title").getString(), () -> {
                    Dispatcher.sendToServer(new EventsRequestedPacket());
                });

                CubeImGui.menuItem("States \ueb67", () -> {
                    Dispatcher.sendToServer(new StatesRequestedPacket());
                });
            });

            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.settings.title").getString(), () -> {
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.dashboard.settings.title").getString(), () -> {
                    ImGuiLoader.pushView(new SettingsView());
                });
            });

            CubeImGui.menu("Dev", () -> {
                CubeImGui.menuItem("Dev", () -> {
                    ImGuiLoader.pushView(new DevView());
                });
            });

            ImGui.setCursorPosX(ImGui.getWindowWidth() - ImGui.getFontSize() * 3 - ImGui.getStyle().getItemSpacingX() * 6);

            CubeImGui.imageButton(Icons.SAVE, Text.translatable("imgui.cubecode.dashboard.saveWindows.title").getString(), ImGui.getFontSize(), ImGui.getFontSize(), () -> {
                CubeCodeClient.viewDataManager.clearViewsData();

                for (View view : ImGuiLoader.getViews()) {
                    if (view instanceof TestView || view instanceof DashboardView)
                        continue;

                    CubeCodeClient.viewDataManager.addViewData(view.getClass().getName()+"#"+view.getUniqueID(), new ViewDataManager.ViewData(
                            view.windowPos.x,
                            view.windowPos.y,
                            view.windowSize.x,
                            view.windowSize.y,
                            view.windowCollapsed,
                            view.serializeData()
                    ));
                }
            });

            ImGui.sameLine();

            CubeImGui.imageButton(Icons.LLAMA, Text.translatable("imgui.cubecode.dashboard.support.title").getString(), ImGui.getFontSize(), ImGui.getFontSize(), () -> {
                Util.getOperatingSystem().open("https://boosty.to/jenyuyhj");
            });

            ImGui.sameLine();

            CubeImGui.imageButton(Icons.DISCORD, Text.translatable("imgui.cubecode.dashboard.discord.title").getString(), ImGui.getFontSize(), ImGui.getFontSize(), () -> {
                Util.getOperatingSystem().open("https://discord.gg/wjYnZGSKjT");
            });
        });


        ImGui.dockSpaceOverViewport(ImGui.getMainViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }

    @Override
    public void onClose() {
        CubeCodeClient.viewDataManager.addAllSessionView(ImGuiLoader.getViews().stream().filter(view -> !(view instanceof DashboardView)).toList());
    }
}

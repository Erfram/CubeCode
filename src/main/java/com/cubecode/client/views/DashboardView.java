package com.cubecode.client.views;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.views.settings.ScriptView;
import com.cubecode.client.views.settings.SettingsView;
import com.cubecode.client.views.settings.WindowView;
import com.cubecode.client.views.textEditor.TextEditorView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import imgui.*;
import imgui.flag.ImGuiDockNodeFlags;
import net.minecraft.text.Text;

public class DashboardView extends View {
    @Override
    public void render() {
        ImGui.pushFont(CubeCodeClient.fontManager.fonts.get(CubeCodeClient.fontManager.currentFontName));
        CubeImGui.mainMenuBar(() -> {
            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.windows.title").getString(), () -> {
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.windows.codeEditor.title").getString(), () -> {
                    ImGuiLoader.pushView(new TextEditorView());
                });

                CubeImGui.menuItem("liray editor", () -> {
                    ImGuiLoader.pushView(new CodeEditorView());
                });

                CubeImGui.menuItem("events", () -> {
                    Dispatcher.sendToServer(new EventsRequestedPacket());
                });
            });

            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.settings.title").getString(), () -> {
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.settings.window.title").getString(), () -> ImGuiLoader.pushView(new WindowView()));
                CubeImGui.menuItem(Text.translatable("imgui.cubecode.settings.script.title").getString(), () -> ImGuiLoader.pushView(new ScriptView()));
                CubeImGui.menuItem("Настройки", () -> ImGuiLoader.pushView(new SettingsView()));
            });
        });

        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
        ImGui.popFont();
    }
}

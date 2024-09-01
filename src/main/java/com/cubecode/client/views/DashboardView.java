package com.cubecode.client.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.views.textEditor.TextEditorView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import imgui.*;
import imgui.flag.ImGuiDockNodeFlags;
import net.minecraft.text.Text;

public class DashboardView extends View {
    @Override
    public void render() {
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
                CubeImGui.menuItem("Настройки", () -> ImGuiLoader.pushView(new SettingsView()));
            });

            CubeImGui.menu("Debug", () -> {
                CubeImGui.menuItem("Style Editor", () -> {
                    ImGuiLoader.pushView(new View() {
                        @Override
                        public void init() {
                            float posX = (windowWidth - 500) * 0.5f;
                            float posY = (windowHeight - 400) * 0.5f;

                            ImGui.setNextWindowPos(posX, posY);
                            ImGui.setNextWindowSize(500, 400);
                        }

                        @Override
                        public void render() {
                            ImGui.showStyleEditor();
                        }
                    });
                });
            });
        });

        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }
}

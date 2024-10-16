package com.cubecode.client.views;

import com.cubecode.api.utils.GsonManager;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDEARequestedPacket;
import com.cubecode.utils.Icons;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import imgui.*;
import imgui.flag.ImGuiDockNodeFlags;
import net.minecraft.text.Text;

public class DashboardView extends View {
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

//            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.debug.title").getString(), () -> {
//                CubeImGui.menuItem(Text.translatable("imgui.cubecode.debug.styleEditor.title").getString(), () -> {
//                    ImGuiLoader.pushView(new View() {
//                        @Override
//                        public void init() {
//                            float posX = (windowWidth - 500) * 0.5f;
//                            float posY = (windowHeight - 400) * 0.5f;

//                            ImGui.setNextWindowPos(posX, posY);
//                            ImGui.setNextWindowSize(500, 400);
//                        }

//                        @Override
//                        public void render() {
//                            ImGui.showStyleEditor();
//                        }
//                    });
//                });
//            });

//            CubeImGui.menu(Text.translatable("imgui.cubecode.dashboard.dev.title").getString(), () -> {
//                CubeImGui.menuItem("Liray Code Editor", () -> ImGuiLoader.pushView(new CodeEditorView()));
//            });

//            float windowWidth = ImGui.getWindowWidth();
//            float menuBarHeight = ImGui.getFrameHeight();
//
//            // Вычисляем позицию для иконки
//            float iconSize = menuBarHeight - 4;
//            float iconPosX = windowWidth - iconSize - 10;
//
//            ImGui.setCursorPosX(iconPosX);
//
//            CubeImGui.imageButton(Icons.SAVE, Text.translatable("imgui.cubecode.dashboard.saveWindows.title").getString(), 16, 16, () -> {
//                JsonObject windows = new JsonObject();
//
//                for (View view : ImGuiLoader.getViews()) {
//                    JsonObject properties = new JsonObject();
//
//                    JsonArray position = new JsonArray();
//
//                    position.add(view.windowPos.x);
//                    position.add(view.windowPos.y);
//
//                    JsonArray size = new JsonArray();
//
//                    size.add(view.windowSize.x);
//                    size.add(view.windowSize.y);
//
//                    properties.add("position", position);
//                    properties.add("size", size);
//                    properties.addProperty("collapsed", view.windowCollapsed);
//
//                    windows.add(view.getName(), properties);
//                }
//
//                GsonManager.writeJSON(CubeCodeConfig.saveWindows.toFile(), windows);
//            });
        });

        ImGui.dockSpaceOverViewport(ImGui.getWindowViewport(), ImGuiDockNodeFlags.NoCentralNode | ImGuiDockNodeFlags.PassthruCentralNode);
    }
}

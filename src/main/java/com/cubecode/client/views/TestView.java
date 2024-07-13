package com.cubecode.client.views;

import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TestView extends View {
    @Override
    public void render() {
        CubeImGui.begin(this, "DragIntBegin", (close) -> {

            String[] items = {"lox", "da"};
            CubeImGui.combo(this, "dragFloat", 1, items, (item) -> {
                MinecraftClient.getInstance().player.sendMessage(Text.of(String.valueOf(item)));
            });

            CubeImGui.imageButton(1, 50, 50, () -> {
                MinecraftClient.getInstance().player.sendMessage(Text.of("true"));
            });

            CubeImGui.dragFloat(this, 4, "drag", 0.001F, 0, 1, (drag) -> {
                MinecraftClient.getInstance().player.sendMessage(Text.of(String.valueOf(drag)));
            });
        });
    }

    @Override
    public void init() {
        int width = 500;
        int height = 400;
        try {
            int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
            int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

            ImGui.setNextWindowPos(width / 2f - windowWidth / 2f, height / 2f - windowHeight / 2f);
        } catch (Exception ignored) {
        }
        ImGui.setNextWindowSize(width, height);
    }
}

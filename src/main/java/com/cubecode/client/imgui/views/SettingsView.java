package com.cubecode.client.imgui.views;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;

public class SettingsView extends View {
    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void render() {
        Window.create()
            .callback((cig) -> {
                ImGui.text("Scale");
                ImGui.sameLine();
                cig.sliderInt("##Scale", MinecraftClient.getInstance().options.getGuiScale().getValue());
            })
            .render(this);

    }
}

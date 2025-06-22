package com.cubecode.client.imgui.views;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.config.ClientConfigManager;
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
    public String getName() {
        return "Settings##"+this.getUUID();
    }

    @Override
    protected void render() {
        Window.create()
            .title(this.getName())
            .callback((cig) -> {
                ImGui.text("Scale");
                ImGui.sameLine();
                cig.sliderInt("##Scale", CubeCodeClient.getConfig().getViewScale());

                if (ImGui.button("Применить")) {
                    int[] variable = this.getVariable("##Scale");
                    CubeCodeClient.getConfig().setViewScale(variable[0]);
                    ClientConfigManager.saveConfig();
                }
            })
            .render(this);

    }
}

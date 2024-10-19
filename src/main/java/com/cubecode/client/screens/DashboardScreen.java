package com.cubecode.client.screens;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.cubecode.client.views.DashboardView;
import com.cubecode.client.imgui.basic.ImGuiLoader;

public class DashboardScreen extends Screen {
    public DashboardScreen() {
        super(Text.of("Dashboard"));
        ImGuiLoader.pushView(new DashboardView());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void close() {
        super.close();
        ImGui.getIO().clearInputKeys();
        ImGuiLoader.clearViews();
    }
}
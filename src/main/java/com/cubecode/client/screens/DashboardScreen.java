package com.cubecode.client.screens;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.ImGuiScreen;
import com.cubecode.client.imgui.views.DashboardView;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DashboardScreen extends ImGuiScreen {
    public DashboardScreen() {
        super(Text.of("Dashboard"), new DashboardView());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context) {
    }

    @Override
    public void close() {
        super.close();
        ImGui.getIO().clearInputKeys();
        ImGuiLoader.clearViews();
    }
}

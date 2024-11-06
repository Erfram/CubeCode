package com.cubecode.client.screens;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import com.cubecode.client.views.DashboardView;
import com.cubecode.client.imgui.basic.ImGuiLoader;

public class DashboardScreen extends Screen {
    public static DrawContext drawContext;

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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        drawContext = context;
    }

    @Override
    public void close() {
        super.close();
        ImGui.getIO().clearInputKeys();
        ImGuiLoader.clearViews();
    }
}
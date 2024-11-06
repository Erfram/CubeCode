package com.cubecode.client.screens;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class TestScreen extends Screen {
    boolean isBackground;
    boolean shouldPause;

    public TestScreen(boolean isBackground, boolean shouldPause, View view) {
        super(Text.of("Test"));

        ImGuiLoader.clearViews();

        ImGuiLoader.pushView(view);

        this.isBackground = isBackground;
        this.shouldPause = shouldPause;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.isBackground) {
            super.renderBackground(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldPause() {
        return shouldPause;
    }

    @Override
    public void close() {
        super.close();
        ImGui.getIO().clearInputKeys();
        ImGuiLoader.clearViews();
    }
}

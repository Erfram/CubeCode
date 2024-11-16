package com.cubecode.client.screens;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ImGuiScreen extends Screen {
    protected ImGuiScreen(Text title) {
        super(title);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        ImGuiLoader.IMGUI_GLFW.keyCallback(MinecraftClient.getInstance().getWindow().getHandle(), keyCode, scanCode, GLFW.GLFW_PRESS, modifiers);
        ImGuiLoader.handleKeyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        ImGuiLoader.IMGUI_GLFW.keyCallback(MinecraftClient.getInstance().getWindow().getHandle(), keyCode, scanCode, GLFW.GLFW_RELEASE, modifiers);
        ImGuiLoader.handleKeyReleased(keyCode, scanCode, modifiers);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        ImGuiLoader.IMGUI_GLFW.scrollCallback(MinecraftClient.getInstance().getWindow().getHandle(), horizontalAmount, verticalAmount);
        ImGuiLoader.handleScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ImGuiLoader.IMGUI_GLFW.mouseButtonCallback(MinecraftClient.getInstance().getWindow().getHandle(), button, GLFW.GLFW_PRESS, 0);
        ImGuiLoader.handleMouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        ImGuiLoader.IMGUI_GLFW.mouseButtonCallback(MinecraftClient.getInstance().getWindow().getHandle(), button, GLFW.GLFW_RELEASE, 0);
        ImGuiLoader.handleMouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        ImGuiLoader.onClose();
        return super.shouldCloseOnEsc();
    }
}

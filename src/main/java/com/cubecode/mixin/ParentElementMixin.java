package com.cubecode.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ParentElement;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.cubecode.client.imgui.basic.ImGuiLoader;

@Mixin(ParentElement.class)
public interface ParentElementMixin {

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ImGuiLoader.IMGUI_GLFW.keyCallback(MinecraftClient.getInstance().getWindow().getHandle(), keyCode, scanCode, GLFW.GLFW_PRESS, modifiers);
    }

    @Inject(method = "keyReleased", at = @At("HEAD"))
    private void onKeyReleased(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ImGuiLoader.IMGUI_GLFW.keyCallback(MinecraftClient.getInstance().getWindow().getHandle(), keyCode, scanCode, GLFW.GLFW_RELEASE, modifiers);
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    private void onMouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        ImGuiLoader.IMGUI_GLFW.scrollCallback(MinecraftClient.getInstance().getWindow().getHandle(), horizontalAmount, verticalAmount);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        ImGuiLoader.IMGUI_GLFW.mouseButtonCallback(MinecraftClient.getInstance().getWindow().getHandle(), button, GLFW.GLFW_PRESS, 0);
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        ImGuiLoader.IMGUI_GLFW.mouseButtonCallback(MinecraftClient.getInstance().getWindow().getHandle(), button, GLFW.GLFW_RELEASE, 0);
    }

}

package com.cubecode.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.cubecode.client.imgui.basic.ImGuiLoader;

@Mixin(value = RenderSystem.class, remap = false)
public abstract class RenderSystemMixin {

    @Inject(method = "flipFrame", at = @At("HEAD"))
    private static void injectFrameRender(CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("Imgui start");
        ImGuiLoader.onFrameRender();
        MinecraftClient.getInstance().getProfiler().pop();
    }

}

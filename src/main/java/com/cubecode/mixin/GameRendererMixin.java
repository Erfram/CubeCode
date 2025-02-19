package com.cubecode.mixin;

import com.cubecode.state.ServerState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void hideHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (this.client.player.getServer() != null && ServerState.getPlayerState(this.client.player).getCubeValues().getBoolean("isHandRender")) {
            ci.cancel();
        }
    }
}

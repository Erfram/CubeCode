package com.cubecode.mixin;

import com.cubecode.client.imgui.textEditor.CubeTextEditor2;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(method = "onChar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;"))
    private void onOnChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        CubeTextEditor2.textEditors.forEach(textEditor -> {
            textEditor.typeChar((char) codePoint);
        });
    }
}

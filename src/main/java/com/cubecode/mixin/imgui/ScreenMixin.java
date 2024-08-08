package com.cubecode.mixin.imgui;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Inject(method = "shouldCloseOnEsc", at = @At("HEAD"))
    public void onShouldCloseOnEsc(CallbackInfoReturnable<Boolean> cir) {
        ImGuiLoader.onClose();
    }
}

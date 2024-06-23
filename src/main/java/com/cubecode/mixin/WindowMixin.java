package com.cubecode.mixin;

import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.cubecode.client.imgui.basic.ImGuiLoader;

@Mixin(Window.class)
public abstract class WindowMixin {

    @Shadow @Final private long handle;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onWindowInit(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String videoMode, String title, CallbackInfo ci){
        ImGuiLoader.onGlfwInit(handle);
    }

}

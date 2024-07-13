package com.cubecode.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.fabricmc.fabric.impl.registry.sync.RemappableRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = RegistrySyncManager.class, remap = true)
public abstract class RegistrySyncManagerMixin {
    @Inject(method = "checkRemoteRemap", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/registry/sync/RemapException;<init>(Lnet/minecraft/text/Text;)V"), cancellable = true)
    private static void onCheckRemoteRemap(Map<Identifier, Object2IntMap<Identifier>> map, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "unmap", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onUnmap(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/registry/sync/RemappableRegistry;remap(Ljava/lang/String;Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/fabricmc/fabric/impl/registry/sync/RemappableRegistry$RemapMode;)V"), cancellable = true, remap = false)
    private static void onApply(Map<Identifier, Object2IntMap<Identifier>> map, RemappableRegistry.RemapMode mode, CallbackInfo ci) {
        ci.cancel();
    }
}
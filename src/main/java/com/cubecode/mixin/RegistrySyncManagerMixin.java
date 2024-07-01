package com.cubecode.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistrySyncManager.class)
public abstract class RegistrySyncManagerMixin {
    @Inject(method = "checkRemoteRemap", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/registry/sync/RemapException;<init>(Lnet/minecraft/text/Text;)V"), cancellable = true)
    private static void onCheckRemoteRemap(Map<Identifier, Object2IntMap<Identifier>> map, CallbackInfo ci) {
        ci.cancel();
    }
}

package com.cubecode.mixin;

import com.cubecode.utils.CubeRegistry;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements CubeRegistry<T> {
    @Inject(method = "freeze", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"), cancellable = true)
    public void onFreeze(CallbackInfoReturnable<Registry<T>> cir) {
        cir.setReturnValue((Registry<T>) Registries.BLOCK);
    }

    @Shadow
    private boolean frozen;

    @Shadow private @Nullable Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;

    @Shadow public abstract RegistryEntry.Reference<T> set(int rawId, RegistryKey<T> key, T value, Lifecycle lifecycle);

    @Shadow private int nextId;

    @Shadow public abstract Registry<T> freeze();

    @Shadow public abstract @Nullable T get(int index);

    @Shadow protected abstract void assertNotFrozen(RegistryKey<T> key);

    @Shadow @Final private Map<RegistryKey<T>, RegistryEntry.Reference<T>> keyToEntry;

    @Shadow @Final private Reference2IntMap<T> entryToRawId;

    @Shadow @Final private Map<Identifier, RegistryEntry.Reference<T>> idToEntry;

    @Shadow @Final private Map<T, RegistryEntry.Reference<T>> valueToEntry;

    @Shadow @Final private ObjectList<RegistryEntry.Reference<T>> rawIdToEntry;

    @Shadow @Final private Map<T, Lifecycle> entryToLifecycle;

    @Shadow private @Nullable List<RegistryEntry.Reference<T>> cachedEntries;

    @Shadow public abstract boolean contains(RegistryKey<T> key);

    @Override
    public void unFreeze() {
        this.frozen = false;
        this.intrusiveValueToEntry = new IdentityHashMap<>();
    }

    @Override
    public RegistryEntry.Reference<T> set(int id, Registry<T> registry, Identifier identifier, T entry) {
        return this.set(id, RegistryKey.of(registry.getKey(), identifier), entry, Lifecycle.stable());
    }

    @Override
    public RegistryEntry.Reference<T> remove(Registry<T> registry, Identifier identifier) {
        RegistryKey<T> key = RegistryKey.of(registry.getKey(), identifier);
        this.assertNotFrozen(key);
        Validate.notNull(key);

        RegistryEntry.Reference<T> reference = this.keyToEntry.remove(key);
        if (reference == null) {
            return null;
        }

        T value = reference.value();
        int rawId = this.entryToRawId.remove(value);

        this.idToEntry.remove(key.getValue());
        this.valueToEntry.remove(value);
        this.rawIdToEntry.set(rawId, null);

        if (this.intrusiveValueToEntry != null) {
            this.intrusiveValueToEntry.put(value, reference);
        }

        this.entryToLifecycle.remove(value);

        this.cachedEntries = null;

        if (rawId == this.nextId - 1) {
            this.nextId = this.findNextAvailableId();
        }

        return reference;
    }

    @Unique
    private int findNextAvailableId() {
        for (int i = this.nextId - 2; i >= 0; i--) {
            if (this.rawIdToEntry.get(i) != null) {
                return i + 1;
            }
        }
        return 0;
    }

    @Override
    public int getNextId() {
        return this.nextId;
    }
}
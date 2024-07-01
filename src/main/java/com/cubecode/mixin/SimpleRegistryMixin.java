package com.cubecode.mixin;

import com.cubecode.utils.CubeRegistry;
import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements CubeRegistry<T> {
    @Shadow
    private boolean frozen;

    @Shadow private @Nullable Map<T, RegistryEntry.Reference<T>> intrusiveValueToEntry;

    @Shadow public abstract RegistryEntry.Reference<T> set(int rawId, RegistryKey<T> key, T value, Lifecycle lifecycle);

    @Shadow private int nextId;

    @Shadow public abstract Registry<T> freeze();

    @Shadow public abstract @Nullable T get(int index);

    @Override
    public void unFreeze() {
        this.frozen = false;
        this.intrusiveValueToEntry = new IdentityHashMap<>();
    }

    @Override
    public RegistryEntry.Reference<T> set(int id, DefaultedRegistry<T> registry, Identifier identifier, T entry) {
        return this.set(id, RegistryKey.of(registry.getKey(), identifier), entry, Lifecycle.stable());
    }

    @Override
    public int getNextId() {
        return this.nextId;
    }
}
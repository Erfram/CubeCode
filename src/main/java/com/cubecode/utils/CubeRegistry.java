package com.cubecode.utils;

import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import com.cubecode.mixin.SimpleRegistryMixin;

public interface CubeRegistry<T> {
    /** {@link SimpleRegistryMixin#unFreeze()} ()} */
    void unFreeze();
    /** {@link SimpleRegistryMixin#freeze()} */
    Registry<T> freeze();

    /** {@link SimpleRegistryMixin#set} */
    RegistryEntry.Reference<T> set(int id, DefaultedRegistry<T> registry, Identifier identifier, T entry);
    /** {@link SimpleRegistryMixin#remove} */
    RegistryEntry.Reference<T> remove(DefaultedRegistry<T> registry, Identifier identifier);

    int getNextId();
}

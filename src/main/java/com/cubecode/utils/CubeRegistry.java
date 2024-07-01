package com.cubecode.utils;

import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface CubeRegistry<T> {
    void unFreeze();
    Registry<T> freeze();

    RegistryEntry.Reference<T> set(int id, DefaultedRegistry<T> registry, Identifier identifier, T entry);
    int getNextId();
}

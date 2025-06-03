package com.cubecode.utils;

import net.minecraft.nbt.NbtCompound;

public interface Serializable {
    NbtCompound serialize();
    void deserialize(NbtCompound nbt);
}

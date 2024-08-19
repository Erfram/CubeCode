package com.cubecode.state;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class PlayerState {

    private NbtCompound values = new NbtCompound();

    public NbtCompound getValues() {
        return this.values;
    }

    public void setValues(NbtCompound values) {
        this.values = values;
    }
}

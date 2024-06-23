package com.cubecode.state;

import net.minecraft.nbt.NbtCompound;

public class PlayerState {

    private NbtCompound values = new NbtCompound();

    public NbtCompound getValues() {
        return this.values;
    }

    public void setValues(NbtCompound values) {
        this.values = values;
    }

}

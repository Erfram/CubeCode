package com.cubecode.state;

import net.minecraft.nbt.NbtCompound;

public class PlayerState {
    private NbtCompound values = new NbtCompound();
    private NbtCompound cubeValues = new NbtCompound();

    public NbtCompound getValues() {
        return this.values;
    }

    public void setValues(NbtCompound values) {
        this.values = values;
    }

    public NbtCompound getCubeValues() {
        return this.cubeValues;
    }

    public void setCubeValues(NbtCompound cubeValues) {
        this.cubeValues = cubeValues;
    }
}

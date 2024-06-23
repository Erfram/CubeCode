package com.cubecode.api.scripts.code.nbt;

public interface Nbt {
    boolean isCompound();

    boolean isList();

    String stringify();

    boolean isEmpty();

    int size();

    Nbt copy();

    void combine(Nbt nbt);

    boolean isSame(Nbt nbt);
}

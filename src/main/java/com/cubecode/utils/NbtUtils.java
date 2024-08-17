package com.cubecode.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

public class NbtUtils {
    public static boolean containsString(NbtList nbtList, String searchString) {
        for (NbtElement element : nbtList) {
            if (element instanceof NbtString) {
                NbtString nbtString = (NbtString) element;
                if (nbtString.asString().equals(searchString)) {
                    return true;
                }
            } else if (element instanceof NbtCompound) {
                NbtCompound compound = (NbtCompound) element;
                for (String key : compound.getKeys()) {
                    if (compound.get(key) instanceof NbtString) {
                        if (compound.getString(key).equals(searchString)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import com.cubecode.api.utils.GsonManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.cubecode.api.scripts.code.blocks.ScriptBlockState;

import java.util.Map;

public class ScriptFactory {
    public ScriptBlockState createBlockState(String blockId) {
        Block block = Registries.BLOCK.get(new Identifier(blockId));

        return ScriptBlockState.create(block.getDefaultState());
    }

    public ScriptNbtCompound createCompound(String nbt) {
        NbtCompound tag = new NbtCompound();

        if (nbt != null) {
            tag = jsonToNbt(GsonManager.readJSON(nbt, JsonObject.class));
        }

        return new ScriptNbtCompound(tag);
    }

    private static NbtCompound jsonToNbt(JsonObject jsonObject) {
        NbtCompound nbtCompound = new NbtCompound();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                nbtCompound.put(key, jsonToNbt(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                nbtCompound.put(key, jsonArrayToNbtList(value.getAsJsonArray()));
            } else if (value.isJsonPrimitive()) {
                nbtCompound.putString(key, value.getAsString());
            }
        }
        return nbtCompound;
    }

    private static NbtList jsonArrayToNbtList(JsonArray jsonArray) {
        NbtList nbtList = new NbtList();
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                nbtList.add(jsonToNbt(element.getAsJsonObject()));
            } else if (element.isJsonArray()) {
                nbtList.add(jsonArrayToNbtList(element.getAsJsonArray()));
            } else if (element.isJsonPrimitive()) {
                nbtList.add(NbtString.of(element.getAsString()));
            }
        }
        return nbtList;
    }

    public String getClassName(Object value) {
        String classes = value.getClass().toString();
        int beginIndex = classes.lastIndexOf(".") + 1;

        return classes.substring(beginIndex);
    }

    public ScriptVector vector(double x, double y, double z) {
        return new ScriptVector(x, y, z);
    }
}
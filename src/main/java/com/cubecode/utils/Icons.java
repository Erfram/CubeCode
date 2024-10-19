package com.cubecode.utils;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public enum Icons {
    APPEARANCE(),
    THEME(),
    BLOCK(),
    BLOCK_ENTITY(),
    CLIPBOARD(),
    CUBECODE(),
    EMPTY(),
    ENTITY(),
    FLAG(),
    FABRIC(),
    FACTORY(),
    FOLDER(),
    MODULE(),
    INFO(),
    INVENTORY(),
    ITEM(),
    ITEM_STACK(),
    LLAMA(),
    MAGMAOUT(),
    MATH(),
    MINUS(),
    NBT_COMPOUND(),
    NBT_LIST(),
    PLAYER(),
    PLUS(),
    QUESTION(),
    RAY_TRACE(),
    RESET(),
    SAVE(),
    SEARCH(),
    SERVER(),
    STATE(),
    STICK(),
    VECTOR(),
    WORLD(),
    START(),
    BOOK(),
    CUT(),
    DELETE(),
    EDIT(),
    COPY(),
    PASTE(),
    HAMMER(),
    JS(),
    LUA();

    final int glId;

    Icons() {
        this.glId = registerIcon(this.name().toLowerCase());
    }

    private int registerIcon(String path) {
        return MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier(CubeCode.MOD_ID, "imgui/icons/" + path + ".png")).getGlId();
    }

    public static void register() {

    }

    public int getGlId() {
        return this.glId;
    }
}

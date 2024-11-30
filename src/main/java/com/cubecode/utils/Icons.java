package com.cubecode.utils;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum Icons {
    ACCEPT(),
    APPEARANCE(),
    THEME(),
    BLOCK(),
    BLOCK_ENTITY(),
    CLIPBOARD(),
    CLIENT(),
    CUBECODE(),
    DISCORD(),
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
        Identifier iconIdentifier = new Identifier(CubeCode.MOD_ID, "imgui/icons/" + path + ".png");
        int icon = MinecraftClient.getInstance().getTextureManager().getTexture(iconIdentifier).getGlId();

        GlStateManager._bindTexture(icon);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        return icon;
    }

    public static void register() {

    }

    public int getGlId() {
        return this.glId;
    }
}

package com.cubecode.utils;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Locale;

public class Icons {
    public static int RESET;
    public static int SAVE;
    public static int INFO;
    public static int EMPTY;
    public static int FOLDER;
    public static int SEARCH;
    public static int LLAMA;

    //DOCUMENTATION
    public static int PLAYER;
    public static int ENTITY;
    public static int STICK;
    public static int PICKAXE;

    private static final String iconsPath = "imgui/icons/";

    public static void register() {
        RESET = registerIcon("reset");
        SAVE = registerIcon("save");
        INFO = registerIcon("info");
        EMPTY = registerIcon("empty");
        FOLDER = registerIcon("folder");
        SEARCH = registerIcon("search");
        LLAMA = registerIcon("llama");

        PLAYER = registerIcon("player");
        ENTITY = registerIcon("entity");
        STICK = registerIcon("stick");
        PICKAXE = registerIcon("pickaxe");
    }

    private static int registerIcon(String path) {
        return MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier(CubeCode.MOD_ID, iconsPath + path + ".png")).getGlId();
    }

    public static Integer getIcon(@Nullable String name) {
        try {
            Field field = Icons.class.getDeclaredField(name.toUpperCase());
            field.setAccessible(true);

            return (int) field.get(Icons.class);
        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {
            return null;
        }
    }
}

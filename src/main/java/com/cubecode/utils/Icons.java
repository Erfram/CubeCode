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
    public static int CLIPBOARD;
    public static int SERVER;
    public static int PLUS;
    public static int MINUS;
    public static int FABRIC;
    public static int CUBE_CODE;
    public static int MAGMAOUT;
    public static int QUESTION;
    public static int WAIT;

    //DOCUMENTATION
    public static int PLAYER;
    public static int ENTITY;
    public static int ARROW;
    public static int PICKAXE;
    public static int ITEM;
    public static int WORLD;
    public static int CONTAINER;
    public static int NBT_COMPOUND;
    public static int NBT_LIST;
    public static int RAY_TRACE;
    public static int BLOCK;
    public static int SMILE_BLOCK;
    public static int VECTOR;
    public static int FLAG;
    public static int WRENCH;
    public static int STATES;

    private static final String iconsPath = "imgui/icons/";

    public static void register() {
        RESET = registerIcon("reset");
        SAVE = registerIcon("save");
        INFO = registerIcon("info");
        EMPTY = registerIcon("empty");
        FOLDER = registerIcon("folder");
        SEARCH = registerIcon("search");
        LLAMA = registerIcon("llama");
        CLIPBOARD = registerIcon("clipboard");
        SERVER = registerIcon("server");
        PLUS = registerIcon("plus");
        MINUS = registerIcon("minus");
        FABRIC = registerIcon("fabric");
        CUBE_CODE = registerIcon("cubecode");
        MAGMAOUT = registerIcon("magmaout");
        QUESTION = registerIcon("question");
        WAIT = registerIcon("wait");

        PLAYER = registerIcon("player");
        ENTITY = registerIcon("entity");
        ARROW = registerIcon("arrow");
        PICKAXE = registerIcon("pickaxe");
        ITEM = registerIcon("item");
        WORLD = registerIcon("world");
        CONTAINER = registerIcon("container");
        NBT_COMPOUND = registerIcon("nbt_compound");
        NBT_LIST = registerIcon("nbt_list");
        RAY_TRACE = registerIcon("ray_trace");
        BLOCK = registerIcon("block");
        SMILE_BLOCK = registerIcon("smile_block");
        VECTOR = registerIcon("vector");
        FLAG = registerIcon("flag");
        WRENCH = registerIcon("wrench");
        STATES = registerIcon("states");
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

package com.cubecode.utils;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class Icons {
    public static int APPEARENCE;
    public static int BLOCK;
    public static int BLOCK_ENTITY;
    public static int CLIPBOARD;
    public static int CUBECODE;
    public static int EMPTY;
    public static int ENTITY;
    public static int FLAG;
    public static int FABRIC;
    public static int FACTORY;
    public static int INFO;
    public static int FOLDER;
    public static int INVENTORY;
    public static int ITEM;
    public static int ITEM_STACK;
    public static int LLAMA;
    public static int MAGMAOUT;
    public static int MATH;
    public static int MINUS;
    public static int NBT_COMPOUND;
    public static int NBT_LIST;
    public static int PLAYER;
    public static int PLUS;
    public static int QUESTION;
    public static int RAY_TRACE;
    public static int RESET;
    public static int SAVE;
    public static int SEARCH;
    public static int SERVER;
    public static int STATE;
    public static int STICK;
    public static int VECTOR;
    public static int WORLD;

    private static final String iconsPath = "imgui/icons/";

    public static void register() {
        APPEARENCE = registerIcon("appearence");
        BLOCK = registerIcon("block");
        BLOCK_ENTITY = registerIcon("block_entity");
        CLIPBOARD = registerIcon("clipboard");
        CUBECODE = registerIcon("cubecode");
        EMPTY = registerIcon("empty");
        ENTITY = registerIcon("entity");
        FLAG = registerIcon("flag");
        FABRIC = registerIcon("fabric");
        FACTORY = registerIcon("factory");
        FOLDER = registerIcon("folder");
        INFO = registerIcon("info");
        INVENTORY = registerIcon("inventory");
        ITEM = registerIcon("item");
        ITEM_STACK = registerIcon("item_stack");
        LLAMA = registerIcon("llama");
        MAGMAOUT = registerIcon("magmaout");
        MATH = registerIcon("math");
        MINUS = registerIcon("minus");
        NBT_COMPOUND = registerIcon("nbt_compound");
        NBT_LIST = registerIcon("nbt_list");
        PLAYER = registerIcon("player");
        PLUS = registerIcon("plus");
        QUESTION = registerIcon("question");
        RAY_TRACE = registerIcon("ray_trace");
        RESET = registerIcon("reset");
        SAVE = registerIcon("save");
        SEARCH = registerIcon("search");
        SERVER = registerIcon("server");
        STATE = registerIcon("state");
        STICK = registerIcon("stick");
        VECTOR = registerIcon("vector");
        WORLD = registerIcon("world");
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

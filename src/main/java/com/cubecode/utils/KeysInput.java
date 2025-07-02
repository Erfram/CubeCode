package com.cubecode.utils;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeysInput {
    public static Map<Integer, Character> keys = new HashMap<>();

    static {
        putKey(GLFW.GLFW_KEY_Q);
        putKey(GLFW.GLFW_KEY_W);
        putKey(GLFW.GLFW_KEY_E);
        putKey(GLFW.GLFW_KEY_R);
        putKey(GLFW.GLFW_KEY_T);
        putKey(GLFW.GLFW_KEY_Y);
        putKey(GLFW.GLFW_KEY_U);
        putKey(GLFW.GLFW_KEY_I);
        putKey(GLFW.GLFW_KEY_O);
        putKey(GLFW.GLFW_KEY_P);
        putKey(GLFW.GLFW_KEY_A);
        putKey(GLFW.GLFW_KEY_S);
        putKey(GLFW.GLFW_KEY_D);
        putKey(GLFW.GLFW_KEY_F);
        putKey(GLFW.GLFW_KEY_G);
        putKey(GLFW.GLFW_KEY_H);
        putKey(GLFW.GLFW_KEY_J);
        putKey(GLFW.GLFW_KEY_K);
        putKey(GLFW.GLFW_KEY_L);
        putKey(GLFW.GLFW_KEY_Z);
        putKey(GLFW.GLFW_KEY_X);
        putKey(GLFW.GLFW_KEY_C);
        putKey(GLFW.GLFW_KEY_V);
        putKey(GLFW.GLFW_KEY_B);
        putKey(GLFW.GLFW_KEY_N);
        putKey(GLFW.GLFW_KEY_M);
        putKey(GLFW.GLFW_KEY_SLASH);
        putKey(GLFW.GLFW_KEY_KP_DIVIDE);
        putKey(GLFW.GLFW_KEY_KP_MULTIPLY);
        putKey(GLFW.GLFW_KEY_KP_SUBTRACT);
        putKey(GLFW.GLFW_KEY_KP_ADD);
        putKey(GLFW.GLFW_KEY_KP_EQUAL);
        putKey(GLFW.GLFW_KEY_APOSTROPHE);
        putKey(GLFW.GLFW_KEY_COMMA);
        putKey(GLFW.GLFW_KEY_MINUS);
        putKey(GLFW.GLFW_KEY_PERIOD);
        putKey(GLFW.GLFW_KEY_SPACE);
        putKey(GLFW.GLFW_KEY_0);
        putKey(GLFW.GLFW_KEY_1);
        putKey(GLFW.GLFW_KEY_2);
        putKey(GLFW.GLFW_KEY_3);
        putKey(GLFW.GLFW_KEY_4);
        putKey(GLFW.GLFW_KEY_5);
        putKey(GLFW.GLFW_KEY_6);
        putKey(GLFW.GLFW_KEY_7);
        putKey(GLFW.GLFW_KEY_8);
        putKey(GLFW.GLFW_KEY_9);
    }

    private static void putKey(int key) {
        keys.put(key, (char) key);
    }
}

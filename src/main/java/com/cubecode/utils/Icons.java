package com.cubecode.utils;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class Icons {
    public static int RESET;

    private static final String iconsPath = "imgui/icons/";

    public static void register() {
        RESET = MinecraftClient.getInstance().getTextureManager().getTexture(new Identifier(CubeCode.MOD_ID, iconsPath + "reset.png")).getGlId();
    }
}

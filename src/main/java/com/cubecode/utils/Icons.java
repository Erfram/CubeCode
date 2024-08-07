package com.cubecode.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class Icons {
    public static Identifier RESET_BUTTON_TEXTURE;

    private static final String iconsPath = "imgui/icons/";

    public static void register() {
        RESET_BUTTON_TEXTURE = TextureManager.loadTexture(iconsPath + "reset.png");
    }

    public static int getId(Identifier identifier) {
        return MinecraftClient.getInstance().getTextureManager().getTexture(identifier).getGlId();
    }
}

package com.cubecode.client.imgui;

import com.cubecode.CubeCode;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public enum Icons {
    FOLDER,
    JS,
    START;

    final int glId;

    Icons() {
        this.glId = registerIcon(this.name().toLowerCase());
    }

    private int registerIcon(String path) {
        Identifier iconIdentifier = CubeCode.createId("imgui/icons/" + path + ".png");
        MinecraftClient client = MinecraftClient.getInstance();

        client.getTextureManager().bindTexture(iconIdentifier);

        AbstractTexture texture = client.getTextureManager().getTexture(iconIdentifier);
        int icon = texture.getGlId();

        GlStateManager._bindTexture(icon);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

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

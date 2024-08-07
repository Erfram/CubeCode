package com.cubecode.utils;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;

public class TextureManager {
    public static Identifier loadTexture(String path) {
        NativeImage image;
        try (InputStream inputStream = TextureManager.class.getClassLoader().getResourceAsStream(path)) {
            image = NativeImage.read(inputStream);
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            Identifier textureId = new Identifier(CubeCode.MOD_ID, path);

            MinecraftClient.getInstance().getTextureManager().registerTexture(textureId, texture);
            return textureId;
        } catch (Exception e) {
            CubeCode.LOGGER.error(e.getMessage());
            return null;
        }
    }
}

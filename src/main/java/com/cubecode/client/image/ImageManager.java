package com.cubecode.client.image;

import com.cubecode.CubeCodeClient;
import com.cubecode.utils.DirectoryManager;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ImageManager extends DirectoryManager {
    public HashMap<File, Integer> images = new HashMap<>();

    public ImageManager() {
        super(CubeCodeClient.imageDir);

        this.registerImages(this.DIRECTORY);
    }

    public int getImage(String path) {
        Integer i = this.images.get(CubeCodeClient.imageDir.toPath().resolve(path).toFile());

        if (i == null) {
            return -1;
        }

        return i;
    }

    private void registerImages(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                this.registerImages(file);
            } else {
                if (this.images.get(file) == null && file.getName().endsWith(".png")) {

                    NativeImage nativeImage = this.loadImage(file.getPath());

                    NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);

                    TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                    Identifier identifier = textureManager.registerDynamicTexture(
                            file.getName().replace(".png", ""),
                            texture
                    );

                    GlStateManager._bindTexture(textureManager.getTexture(identifier).getGlId());
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                    //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

                    this.images.put(file, textureManager.getTexture(identifier).getGlId());
                }
            }
        }
    }

    private NativeImage loadImage(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            return NativeImage.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

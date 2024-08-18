package com.cubecode.client.gifs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class Gif {

    protected int glID;

    protected float cursor = 0;

    protected float difference;

    protected long velocityTick;

    protected long lastVelocityTick;

    protected boolean available;

    protected boolean collected;

    protected List<BufferedImage> cachedImages;

    public void handle() {
        if (!collected) {
            load();
        }

        ClientWorld world = MinecraftClient.getInstance().world;

        if (available && world != null) {
            long currentTime = world.getTime();

            if ((currentTime - lastVelocityTick) >= velocityTick) {
                if (cursor + difference >= 1.0f) {
                    cursor = 0;
                } else {
                    cursor += difference;
                }

                lastVelocityTick = currentTime;
            }
        }
    }

    public void load() {
        if (cachedImages.isEmpty()) {
            return;
        }

        // Image reference
        BufferedImage zeroChannel = cachedImages.get(0);

        // Total width and height
        int width = zeroChannel.getWidth();
        int height = zeroChannel.getHeight() * cachedImages.size();

        // Buffer 4xChannel
        ByteBuffer buffer = BufferUtils.createByteBuffer(4 * width * height);

        int[] pixels = new int[width * height];

        for (BufferedImage cachedImage : cachedImages) {
            int localWidth = cachedImage.getWidth();
            int localHeight = cachedImage.getHeight();

            cachedImage.getRGB(0, 0, localWidth, localHeight, pixels, 0, localWidth);

            for (int y = 0; y < localHeight; y++) {
                for (int x = 0; x < localWidth; x++) {
                    int pixel = pixels[y * localWidth + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
                    buffer.put((byte) (pixel & 0xFF));         // B
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
                }
            }
        }

        buffer.flip();

        glID = GL32.glGenTextures();
        GL32.glBindTexture(GL32.GL_TEXTURE_2D, glID);

        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_S, GL32.GL_REPEAT);
        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_T, GL32.GL_REPEAT);
        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MIN_FILTER, GL32.GL_NEAREST);
        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MAG_FILTER, GL32.GL_NEAREST);

        GL32.glTexImage2D(GL32.GL_TEXTURE_2D, 0, GL32.GL_RGBA, width, height, 0, GL32.GL_RGBA, GL32.GL_UNSIGNED_BYTE, buffer);

        GL32.glBindTexture(GL32.GL_TEXTURE_2D, 0);

        difference = 1f / cachedImages.size();
        cachedImages = null;
        collected = true;
        available = true;
    }

    public float getCursor() {
        return cursor;
    }

    public float getDifference() {
        return difference;
    }

    public int getGlId() {
        return glID;
    }

    public boolean isAvailable() {
        return available;
    }

    public void unload() {
        if (available) {
            GL32.glDeleteTextures(glID);
        }
    }

    public Gif payload(List<BufferedImage> payload) {
        this.cachedImages = payload;
        this.velocityTick = 0L;
        return this;
    }

    public void velocity(long velocityTick) {
        if (this.velocityTick == velocityTick) return;
        this.velocityTick = velocityTick;
    }

}

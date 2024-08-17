package com.cubecode.utils;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GifManager {
    public static List<Integer> imagesGif = new ArrayList<>();
    public static int cursor = 0;
    public static String lastGifPath = "";
    public static int speed;
    private static long lastTickTime = 0;
    private static List<BufferedImage> images = new ArrayList<>();
    private static boolean needLoad = false;
    private static boolean dirty = false;

    public static Integer getImage() {
        try {
            if (imagesGif.isEmpty() || needLoad) {
                return null;
            }

            if (cursor > imagesGif.size() - 1) {
                return null;
            }

            return imagesGif.get(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void update() {
        long currentTime = MinecraftClient.getInstance().world.getTime();

        if (dirty) {
            clear();
            dirty = false;
        }

        if (needLoad) {
            images.forEach(gifImage -> {
                imagesGif.add(loadTexture(gifImage));
            });
            needLoad = false;
        }

        if ((currentTime - lastTickTime) >= speed) {
            if (cursor + 1 >= imagesGif.size()) {
                cursor = 0;
            } else {
                cursor++;
            }

            lastTickTime = currentTime;
        }
    }

    public static void clear() {
        imagesGif.forEach(GL32::glDeleteTextures);

        imagesGif.clear();
    }

    public static void loadGif(String filePath, int speed) {
        GifManager.speed = speed;
        if (lastGifPath.equals(filePath)) {
            return;
        }

        lastGifPath = filePath;

        CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(CubeCode.MOD_ID, filePath));

                if (resource.isPresent()) {
                    List<BufferedImage> bufferedImages = splitGifToBufferedImages(resource.get().getInputStream());
                    Map.Entry<String, List<BufferedImage>> entry = Map.entry(filePath, bufferedImages);

                    return entry;
                } else {
                    CubeCode.LOGGER.debug(new Identifier(CubeCode.MOD_ID, filePath).toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Map.entry(filePath, new ArrayList<BufferedImage>());
        }).thenAccept((entry) -> {
            if (!entry.getKey().equals(lastGifPath)) {
                return;
            }

            dirty = true;

            if (entry.getValue().isEmpty()) {
                return;
            }
            images = entry.getValue();
            needLoad = true;
        });
    }

    public static List<BufferedImage> splitGifToBufferedImages(InputStream inputStream) throws IOException {
        List<BufferedImage> bufferedImages = new ArrayList<>();

        try (ImageInputStream stream = ImageIO.createImageInputStream(inputStream)) {
            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(stream);

            int numFrames = reader.getNumImages(true);
            BufferedImage masterImage = null;

            for (int i = 0; i < numFrames; i++) {
                BufferedImage image = reader.read(i);

                if (i == 0) {
                    masterImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                }

                Graphics2D g2d = masterImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();

                BufferedImage copy = new BufferedImage(masterImage.getColorModel(),
                        masterImage.copyData(null),
                        masterImage.isAlphaPremultiplied(),
                        null);

                bufferedImages.add(copy);
            }
        }
        return bufferedImages;
    }

    public static int loadTexture(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        ByteBuffer buffer = BufferUtils.createByteBuffer(4 * width * height);

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));  // Красный компонент
                buffer.put((byte) ((pixel >> 8) & 0xFF));   // Зеленый компонент
                buffer.put((byte) (pixel & 0xFF));          // Синий компонент
                buffer.put((byte) ((pixel >> 24) & 0xFF));  // Альфа компонент
            }
        }

        buffer.flip();

        int textureID = GL32.glGenTextures();
        GL32.glBindTexture(GL32.GL_TEXTURE_2D, textureID);

        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_S, GL32.GL_REPEAT);
        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_WRAP_T, GL32.GL_REPEAT);
        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MIN_FILTER, GL32.GL_NEAREST);
        GL32.glTexParameteri(GL32.GL_TEXTURE_2D, GL32.GL_TEXTURE_MAG_FILTER, GL32.GL_NEAREST);

        GL32.glTexImage2D(GL32.GL_TEXTURE_2D, 0, GL32.GL_RGBA, width, height, 0, GL32.GL_RGBA, GL32.GL_UNSIGNED_BYTE, buffer);

        GL32.glBindTexture(GL32.GL_TEXTURE_2D, 0);

        return textureID;
    }
}

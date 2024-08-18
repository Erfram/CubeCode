package com.cubecode.client.gifs;

import com.cubecode.CubeCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GifManager {

    public static final String DEFAULT_GIF = "imgui/gifs/default.gif";
    public static ConcurrentHashMap<String, Gif> gifs = new ConcurrentHashMap<>();
    public static ArrayList<String> requested = new ArrayList<>();

    public static void update() {
        gifs.forEach((s, v) -> v.handle());
    }

    public static void clear() {
        gifs.forEach((s, v) -> v.unload());
        gifs.clear();
        requested.clear();
    }

    public static Optional<Gif> getGif(String filePath) {
        if (gifs.containsKey(filePath)) {
            return Optional.of(gifs.get(filePath));
        } else {
            requestLoadGif(filePath);
        }
        return Optional.empty();
    }

    private static void requestLoadGif(String path) {
        if (requested.contains(path)) return;

        requested.add(path);

        CompletableFuture.runAsync(() -> {
            try {
                if (path.startsWith("http")) {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        List<BufferedImage> images = collectGif(connection.getInputStream());
                        gifs.put(path, new Gif().payload(images));
                    } else {
                        CubeCode.LOGGER.error("Failed to connect to URL: {}", connection.getResponseCode());
                    }
                } else {
                    Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier(CubeCode.MOD_ID, path));

                    if (resource.isPresent()) {
                        List<BufferedImage> images = collectGif(resource.get().getInputStream());
                        gifs.put(path, new Gif().payload(images));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static List<BufferedImage> collectGif(InputStream inputStream) throws IOException {
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

}

package com.cubecode.client.imgui.fonts;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import imgui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    public Map<String, ImFont> fonts = new HashMap<>();

    public String currentFontName = "default";

    public void loadFonts() {
        ImGuiIO io = ImGui.getIO();
        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.clear();

        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

        try (InputStream inputStream = ImGuiLoader.class.getClassLoader().getResourceAsStream("assets/cubecode/imgui/fonts/default.ttf")) {
            byte[] bytes = inputStream.readAllBytes();
            fonts.put("default", fontAtlas.addFontFromMemoryTTF(bytes, 16, new ImFontConfig(), io.getFonts().getGlyphRangesCyrillic()));
        } catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }

        try {
            Files.list(CubeCodeConfig.fontsDir).forEach(fontPath -> {
                if (fontPath.toFile().getName().endsWith(".ttf")) {
                    try (FileInputStream fileInputStream = new FileInputStream(fontPath.toString())) {
                        byte[] bytes = fileInputStream.readAllBytes();
                        fonts.put(fontPath.toFile().getName().replace(".ttf", ""), fontAtlas.addFontFromMemoryTTF(bytes, 16, new ImFontConfig(), io.getFonts().getGlyphRangesCyrillic()));
                    } catch (Exception exception) {
                        CubeCode.LOGGER.error(exception.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            CubeCode.LOGGER.error(e.getMessage());
        }

        fontConfig.destroy();

        fontAtlas.build();

        currentFontName = CubeCodeConfig.getSettingsConfig().general.appearance.font;
    }
}

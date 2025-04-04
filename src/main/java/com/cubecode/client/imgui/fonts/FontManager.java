package com.cubecode.client.imgui.fonts;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import imgui.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FontManager {
    public Map<String, ImFont> fonts = new HashMap<>();

    public String currentFontName = "Monocraft";

    public void loadFonts() {
        ImGuiIO io = ImGui.getIO();
        final ImFontAtlas fontAtlas = io.getFonts();
        fontAtlas.clear();

        ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
        //TODO: REFACTOR BLYAT
        try (InputStream inputStream = ImGuiLoader.class.getClassLoader().getResourceAsStream("assets/cubecode/imgui/fonts/Monocraft.ttf")) {
            byte[] bytes = inputStream.readAllBytes();
            //TODO: В нормальном виде сделать добавление ренжей
            short[] ranges = io.getFonts().getGlyphRangesCyrillic();
            short[] newArray = new short[ranges.length + 2];
            System.arraycopy(ranges, 0, newArray, 0, ranges.length);
            newArray[ranges.length] = 8592;
            newArray[ranges.length + 1] = 8703;
            ImFontConfig imFontConfig = new ImFontConfig();
            imFontConfig.setRasterizerMultiply(1);
            imFontConfig.setOversampleH(3);
            imFontConfig.setOversampleV(3);
            imFontConfig.setPixelSnapH(true);
            fonts.put("Monocraft", fontAtlas.addFontFromMemoryTTF(bytes, 64, imFontConfig, newArray));
        } catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }

        try (InputStream inputStream = ImGuiLoader.class.getClassLoader().getResourceAsStream("assets/cubecode/imgui/fonts/JetBrainsMono-Regular.ttf")) {
            byte[] bytes = inputStream.readAllBytes();
            //TODO: В нормальном виде сделать добавление ренжей
            short[] ranges = io.getFonts().getGlyphRangesCyrillic();
            short[] newArray = new short[ranges.length + 2];
            System.arraycopy(ranges, 0, newArray, 0, ranges.length);
            newArray[ranges.length] = 8592;
            newArray[ranges.length + 1] = 8703;
            ImFontConfig imFontConfig = new ImFontConfig();
            imFontConfig.setRasterizerMultiply(1);
            imFontConfig.setOversampleH(3);
            imFontConfig.setOversampleV(3);
            imFontConfig.setPixelSnapH(true);
            fonts.put("JetBrainsMono-Regular", fontAtlas.addFontFromMemoryTTF(bytes, 64, imFontConfig, newArray));
        } catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }

        try {
            Files.list(CubeCodeConfig.fontsDir).forEach(fontPath -> {
                if (fontPath.toFile().getName().endsWith(".ttf")) {
                    try (FileInputStream fileInputStream = new FileInputStream(fontPath.toString())) {
                        byte[] bytes = fileInputStream.readAllBytes();
                        ImFontConfig imFontConfig = new ImFontConfig();
                        imFontConfig.setRasterizerMultiply(1.0F);
                        imFontConfig.setOversampleH(5);
                        imFontConfig.setOversampleV(5);
                        fonts.put(fontPath.toFile().getName().replace(".ttf", ""), fontAtlas.addFontFromMemoryTTF(bytes, 16, imFontConfig, io.getFonts().getGlyphRangesCyrillic()));
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

    public static List<CubeFont> getFonts() {
        List<CubeFont> fonts = new ArrayList<>();
        for (File fontFile : CubeCodeConfig.fontsDir.toFile().listFiles()) {
            if (fontFile.getName().endsWith(".ttf")) {
                fonts.add(new CubeFont(fontFile.getPath(), 20));
            }
        }

        return fonts;
    }

    public static List<String> getFontNames() {
        List<CubeFont> fonts = getFonts();

        List<String> fontNames = new ArrayList<>();

        fonts.forEach(font -> {
            if (!new File(font.getFilePath()).getName().equalsIgnoreCase("Monocraft.ttf")) {
                fontNames.add(new File(font.getFilePath()).getName().replace(".ttf", ""));
            }
        });

        fontNames.add("Monocraft");
        fontNames.add("JetBrainsMono-Regular");

        return fontNames;
    }
}

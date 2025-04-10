package com.cubecode.client.imgui.fonts;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import imgui.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        ImFontConfig standardFontConfig = new ImFontConfig();
        standardFontConfig.setRasterizerMultiply(1);
        standardFontConfig.setOversampleH(3);
        standardFontConfig.setOversampleV(3);
        standardFontConfig.setPixelSnapH(true);

        //TODO: В нормальном виде сделать добавление ренжей
        short[] ranges = ImGui.getIO().getFonts().getGlyphRangesCyrillic();
        short[] newArray = new short[ranges.length + 2];
        System.arraycopy(ranges, 0, newArray, 0, ranges.length);
        newArray[ranges.length] = 8592;
        newArray[ranges.length + 1] = 8703;

        standardFontConfig.setGlyphRanges(newArray);

        loadFont("Monocraft", ImGuiLoader.class.getClassLoader().getResourceAsStream("assets/cubecode/imgui/fonts/Monocraft.ttf"), fontAtlas, standardFontConfig);
        loadFont("JetBrainsMono-Regular", ImGuiLoader.class.getClassLoader().getResourceAsStream("assets/cubecode/imgui/fonts/JetBrainsMono-Regular.ttf"), fontAtlas, standardFontConfig);

        try {
            Files.list(CubeCodeConfig.fontsDir).forEach(fontPath -> {
                if (!fontPath.toFile().getName().endsWith(".ttf")) {
                    return;
                }
                String name = fontPath.toFile().getName().replace(".ttf", "");
                InputStream inputStream;
                try {
                    inputStream = new FileInputStream(fontPath.toString());


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                loadFont(name, inputStream, fontAtlas, standardFontConfig);
            });
        } catch (IOException e) {
            CubeCode.LOGGER.error(e.getMessage());
        }

        fontAtlas.build();

        standardFontConfig.destroy();

        currentFontName = CubeCodeConfig.getSettingsConfig().general.appearance.font;
    }

    public void loadFont(String name, InputStream inputStream, ImFontAtlas fontAtlas, ImFontConfig imFontConfig) {
        try (InputStream is = inputStream) {
            fonts.put(name, fontAtlas.addFontFromMemoryTTF(is.readAllBytes(), 64, imFontConfig));

            ImFontConfig fontConfig = new ImFontConfig();
            fontConfig.setMergeMode(true); // Режим слияния с основным шрифтом
            fontConfig.setPixelSnapH(true);
            fontConfig.setGlyphOffset(1f, 11f); // Иконки высоковато, опустим пониже
            short[] iconRanges = new short[] {(short) 0xE000, (short) 0xF8FD, 0 };
            InputStream isMaterial = ImGuiLoader.class.getClassLoader().getResourceAsStream("assets/cubecode/imgui/material.ttf");
            fontAtlas.addFontFromMemoryTTF(isMaterial.readAllBytes(), 64, fontConfig, iconRanges);

            fontConfig.destroy();
        }
        catch (Exception exception) {
            CubeCode.LOGGER.error(exception.getMessage());
        }
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
            fontNames.add(new File(font.getFilePath()).getName().replace(".ttf", ""));
        });

        fontNames.add("Monocraft");
        fontNames.add("JetBrainsMono-Regular");

        return fontNames;
    }
}

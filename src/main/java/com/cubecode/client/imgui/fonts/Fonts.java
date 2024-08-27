package com.cubecode.client.imgui.fonts;

import com.cubecode.client.config.CubeCodeConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Fonts {
    public static CubeFont MAIN_FONT = new CubeFont(CubeCodeConfig.fontsDir.resolve(CubeCodeConfig.getSettingsConfig().general.appearance.font + ".ttf").toString(), 20);

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

        return fontNames;
    }
}
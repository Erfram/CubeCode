package com.cubecode.client.imgui.themes;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private final List<CubeTheme> themes = new ArrayList<>();
    public CubeTheme currentTheme = this.getTheme("Catppuccin Mocha");

    public ThemeManager() {
        themes.add(new CubeTheme("Default"));
        themes.add(new CubeTheme("Default dark"));
        themes.add(new CubeTheme("Default light"));
        themes.add(new CubeTheme("Catppuccin Mocha"));
    }

    public void loadThemes() {
        try {
            Files.list(CubeCodeConfig.themesDir).forEach(themePath -> {
                if (themePath.toString().endsWith(".json")) {
                    Gson gson = new Gson();

                    CubeTheme theme = null;
                    try {
                        theme = gson.fromJson(new InputStreamReader(new FileInputStream(themePath.toString())), CubeTheme.class);
                    } catch (FileNotFoundException e) {
                        CubeCode.LOGGER.error(e.getMessage());
                    }

                    if (theme != null) {
                        themes.add(theme);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentTheme = this.getTheme(CubeCodeConfig.getSettingsConfig().general.appearance.theme);
    }

    public List<String> getThemeNames() {
        List<String> themeNames = new ArrayList<>();
        themes.forEach(theme -> {
            themeNames.add(theme.name);
        });

        return themeNames;
    }

    public CubeTheme getTheme(String name) {
        CubeTheme cubeTheme = new CubeTheme("default");
        for (CubeTheme theme : themes) {
            if (theme.name.equals(name)) {
                cubeTheme = theme;
            }
        }

        return cubeTheme;
    }
}
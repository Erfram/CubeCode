package com.cubecode.client.config;

import com.cubecode.api.utils.GsonManager;
import imgui.flag.ImGuiColorEditFlags;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CubeCodeConfig {
    public static final Path configDir = FabricLoader.getInstance().getConfigDir().resolve("cubecode");
    public static final Path settingsDir = configDir.resolve("settings");
    public static final Path fontsDir = configDir.resolve("fonts");
    public static final Path themesDir = configDir.resolve("themes");

    public static final Path settings = configDir.resolve("settings.json");
    public static final Path saveWindows = configDir.resolve("save_windows.json");

    private static SaveWindowsConfig saveWindowsConfig;
    private static SettingsConfig settingsConfig;

    public static final String DEFAULT_FONT = "default";
    public static final String DEFAULT_THEME = "Catppuccin Mocha";

    public static class SaveWindowsConfig {

    }

    public static class SettingsConfig {
        public General general = new General();

        public static class General {
            public Appearance appearance = new Appearance();

            public static class Appearance {
                public String font = DEFAULT_FONT;
                public String theme = DEFAULT_THEME;
            }
        }
    }

    public static void loadConfig() {
        try {
            Files.createDirectories(settingsDir);
            Files.createDirectories(fontsDir);
            Files.createDirectories(themesDir);
            settingsConfig = loadOrCreate(settings, SettingsConfig.class);
            saveWindowsConfig = loadOrCreate(saveWindows, SaveWindowsConfig.class);
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    private static <T> T loadOrCreate(Path path, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        if (Files.notExists(path)) {
            T config = clazz.newInstance();
            GsonManager.writeJSON(path.toFile(), config);
            return config;
        }
        return GsonManager.readJSON(path.toFile(), clazz);
    }

    public static void saveConfig() {
        GsonManager.writeJSON(settings.toFile(), settingsConfig);
        GsonManager.writeJSON(saveWindows.toFile(), saveWindowsConfig);
    }

    public static SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public static SaveWindowsConfig getSaveWindowsConfig() {
        return saveWindowsConfig;
    }
}
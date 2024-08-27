package com.cubecode.client.config;

import com.cubecode.api.utils.GsonManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CubeCodeConfig {
    public static final Path configDir = FabricLoader.getInstance().getConfigDir().resolve("cubecode");
    public static final Path settingsDir = configDir.resolve("settings");
    public static final Path fontsDir = configDir.resolve("fonts");

    private static final Path settings = configDir.resolve("settings.json");
    private static final Path window = settingsDir.resolve("window.json");
    private static final Path script = settingsDir.resolve("script.json");

    private static SettingsConfig settingsConfig;
    private static WindowConfig windowConfig;
    private static ScriptConfig scriptConfig;

    public static final float[] DEFAULT_TITLE_COLOR = new float[] {0.04f, 0.04f, 0.04f, 1.00f};
    public static final float[] DEFAULT_TITLE_ACTIVE_COLOR = new float[] {0.16f, 0.29f, 0.48f, 1.00f};
    public static final float[] DEFAULT_TITLE_COLLAPSED_COLOR = new float[] {0.00f, 0.00f, 0.00f, 0.51f};

    public static final float[] DEFAULT_BG_COLOR = new float[] {0.06f, 0.06f, 0.06f, 0.94f};

    public static final float[] DEFAULT_BORDER_COLOR = new float[] {0.43f, 0.43f, 0.50f, 0.50f};

    public static final float[] DEFAULT_BUTTON_COLOR = new float[] {0.06f, 0.53f, 0.98f, 1f};
    public static final float[] DEFAULT_BUTTON_ACTIVE_COLOR = new float[] {0.26f, 0.59f, 0.98f, 0.40f};
    public static final float[] DEFAULT_BUTTON_HOVERED_COLOR = new float[] {0.26f, 0.59f, 0.98f, 1f};

    public static final float[] DEFAULT_FRAME_BG_COLOR = new float[] { 0.16f, 0.29f, 0.48f, 0.54f };
    public static final float[] DEFAULT_FRAME_BG_ACTIVE_COLOR = new float[] { 0.26f, 0.59f, 0.98f, 0.67f };
    public static final float[] DEFAULT_FRAME_BG_HOVERED_COLOR = new float[] { 0.26f, 0.59f, 0.98f, 0.40f };

    public static final float[] DEFAULT_SLIDER_GRAB_COLOR = new float[] { 0.26f, 0.59f, 0.98f, 0.40f };
    public static final float[] DEFAULT_SLIDER_GRAB_ACTIVE_COLOR = new float[] { 0.26f, 0.59f, 0.98f, 0.40f };

    public static final String DEFAULT_FONT = "default";

    public static String DEFAULT_CONTEXT_NAME = "Context";

    public static class SettingsConfig {
        public General general = new General();

        public static class General {
            public Appearance appearance = new Appearance();

            public static class Appearance {
                public String font = DEFAULT_FONT;
            }
        }
    }

    public static class WindowConfig {
        public float[] titleColor = DEFAULT_TITLE_COLOR;
        public float[] titleActiveColor = DEFAULT_TITLE_ACTIVE_COLOR;
        public float[] titleCollapsedColor = DEFAULT_TITLE_COLLAPSED_COLOR;

        public float[] bgColor = DEFAULT_BG_COLOR;
        public float[] borderColor = DEFAULT_BORDER_COLOR;

        public float[] buttonColor = DEFAULT_BUTTON_COLOR;
        public float[] buttonActiveColor = DEFAULT_BUTTON_ACTIVE_COLOR;
        public float[] buttonHoveredColor = DEFAULT_BUTTON_HOVERED_COLOR;

        public float[] frameBgColor = DEFAULT_FRAME_BG_COLOR;
        public float[] frameBgActiveColor = DEFAULT_FRAME_BG_ACTIVE_COLOR;
        public float[] frameBgHoveredColor = DEFAULT_FRAME_BG_HOVERED_COLOR;

        public float[] sliderGrabColor = DEFAULT_SLIDER_GRAB_COLOR;
        public float[] sliderGrabActiveColor = DEFAULT_SLIDER_GRAB_ACTIVE_COLOR;
    }

    public static class ScriptConfig {
        public String contextName = DEFAULT_CONTEXT_NAME;
    }

    public static void loadConfig() {
        try {
            Files.createDirectories(settingsDir);
            Files.createDirectories(fontsDir);
            settingsConfig = loadOrCreate(settings, SettingsConfig.class);
            windowConfig = loadOrCreate(window, WindowConfig.class);
            scriptConfig = loadOrCreate(script, ScriptConfig.class);
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
        GsonManager.writeJSON(window.toFile(), windowConfig);
        GsonManager.writeJSON(script.toFile(), scriptConfig);
    }

    public static ScriptConfig getScriptConfig() {
        return scriptConfig;
    }

    public static WindowConfig getWindowConfig() {
        return windowConfig;
    }

    public static SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }
}
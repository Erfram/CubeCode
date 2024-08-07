package com.cubecode.client.config;

import com.cubecode.api.utils.GsonManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CubeCodeConfig {
    private static final Path configDir = FabricLoader.getInstance().getConfigDir().resolve("cubecode");
    private static final Path settingsDir = configDir.resolve("settings");
    private static final Path window = settingsDir.resolve("window.json");
    private static final Path script = settingsDir.resolve("script.json");

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

    public static final float[] DEFAULT_SLIDER_GRAB_COLOR = new float[] { 0.26f, 0.59f, 0.98f, 0.40f }; //TODO DEFAULT!!!!
    public static final float[] DEFAULT_SLIDER_GRAB_ACTIVE_COLOR = new float[] { 0.26f, 0.59f, 0.98f, 0.40f }; //TODO DEFAULT!!!!

    public static String DEFAULT_CONTEXT_NAME = "Context";

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

            if (Files.notExists(window)) {
                windowConfig = new WindowConfig();
                saveConfig();
            } else {
                windowConfig = GsonManager.readJSON(window.toFile(), WindowConfig.class);
            }

            if (Files.notExists(script)) {
                scriptConfig = new ScriptConfig();
                saveConfig();
            } else {
                scriptConfig = GsonManager.readJSON(script.toFile(), ScriptConfig.class);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static void saveConfig() {
        GsonManager.writeJSON(window.toFile(), windowConfig);
        GsonManager.writeJSON(script.toFile(), scriptConfig);
    }

    public static ScriptConfig getScriptConfig() {
        return scriptConfig;
    }

    public static WindowConfig getWindowConfig() {
        return windowConfig;
    }
}
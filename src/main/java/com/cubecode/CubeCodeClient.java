package com.cubecode;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.image.ImageManager;
import com.cubecode.client.imgui.basic.window.WindowStateManager;
import com.cubecode.client.imgui.fonts.FontManager;
import com.cubecode.client.imgui.themes.ThemeManager;
import com.cubecode.client.scripts.ClientLoggerManager;
import com.cubecode.client.scripts.ClientProjectManager;
import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.Dispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static final String MOD_ID;

    public static FontManager fontManager;
    public static ThemeManager themeManager;
    public static WindowStateManager windowStateManager;
    public static ClientProjectManager projectManager;
    public static ClientLoggerManager loggerManager;
    public static ImageManager imageManager;

    public static File imageDir = MinecraftClient.getInstance().runDirectory.toPath().resolve("config\\cubecode\\images").toFile();

    public static Logger LOGGER;

    private static Path cubeCodePath;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitializeClient() {
        Dispatcher.register();
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();
        CubeCodeConfig.loadConfig();

        fontManager = new FontManager();
        themeManager = new ThemeManager();
        themeManager.loadThemes();

        cubeCodePath = MinecraftClient.getInstance().runDirectory.toPath().resolve("cubecode");

        projectManager = new ClientProjectManager();
        loggerManager = new ClientLoggerManager(cubeCodePath.toFile());
    }
}
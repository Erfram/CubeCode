package com.cubecode;

import com.cubecode.api.scripts.ScriptExecutor;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.image.ImageManager;
import com.cubecode.client.imgui.basic.ViewDataManager;
import com.cubecode.client.imgui.fonts.FontManager;
import com.cubecode.client.imgui.themes.ThemeManager;
import com.cubecode.client.scripts.ClientProjectManager;
import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.Dispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static final String MOD_ID;

    public static FontManager fontManager;
    public static ThemeManager themeManager;
    public static ClientProjectManager projectManager;
    public static ScriptExecutor scriptExecutor;
    public static ImageManager imageManager;
    public static ViewDataManager viewDataManager;

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

        viewDataManager = new ViewDataManager();
    }
}
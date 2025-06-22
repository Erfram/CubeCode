package com.cubecode;

import com.cubecode.api.project.ProjectManager;
import com.cubecode.config.CommonConfig;
import com.cubecode.config.ConfigManager;
import com.cubecode.network.Dispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.File;

public class CubeCode implements ModInitializer {
    public static final Logger LOGGER;

    public static final String MOD_ID;

    public static File cubeCodeDirectory;
    public static ProjectManager projectManager;

    private static CommonConfig config;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitialize() {
        EventHandler.init();
        Dispatcher.register();

        config = ConfigManager.loadConfig();
    }

    public static Identifier createId(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static CommonConfig getConfig() {
        return config;
    }
}
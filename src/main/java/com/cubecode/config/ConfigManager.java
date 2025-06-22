package com.cubecode.config;

import com.cubecode.utils.GsonManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Path;

public class ConfigManager {
    public static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("cubecode");
    private static final File settingsFile = configPath.resolve("settings.json").toFile();

    public static CommonConfig loadConfig() {
        try {
            if (settingsFile.exists()) {
                return GsonManager.readJson(settingsFile, CommonConfig.class);
            } else {
                configPath.toFile().mkdirs();
                settingsFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CommonConfig defaultConfig = new CommonConfig();
        saveConfig(defaultConfig);
        return defaultConfig;
    }

    public static void saveConfig(CommonConfig config) {
        GsonManager.writeJson(settingsFile, config);
    }
}

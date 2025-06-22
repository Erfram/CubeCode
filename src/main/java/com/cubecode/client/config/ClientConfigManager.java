package com.cubecode.client.config;

import com.cubecode.CubeCodeClient;
import com.cubecode.config.ConfigManager;
import com.cubecode.utils.GsonManager;

import java.io.File;

public class ClientConfigManager {
    private static final File settingsFile = ConfigManager.configPath.resolve("client_settings.json").toFile();

    public static ClientConfig loadConfig() {
        try {
            if (settingsFile.exists()) {
                return GsonManager.readJson(settingsFile, ClientConfig.class);
            } else {
                ConfigManager.configPath.toFile().mkdirs();
                settingsFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClientConfig defaultConfig = new ClientConfig();
        saveConfig(defaultConfig);
        return defaultConfig;
    }

    public static void saveConfig(ClientConfig config) {
        GsonManager.writeJson(settingsFile, config);
    }

    public static void saveConfig() {
        saveConfig(CubeCodeClient.getConfig());
    }
}

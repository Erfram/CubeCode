package com.cubecode;

import com.cubecode.api.events.EventManager;
import com.cubecode.api.scripts.LoggerManager;
import com.cubecode.api.scripts.ScriptExecutor;
import com.cubecode.api.scripts.SettingManager;
import com.cubecode.network.Dispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import com.cubecode.api.scripts.ProjectManager;

import java.io.File;

public class CubeCode implements ModInitializer {
    public static final Logger LOGGER;

    public static final String MOD_ID;

    public static File cubeCodeDirectory;
    public static File factoryDirectory;
    public static File contentDirectory;

    public static ProjectManager projectManager;
    public static ScriptExecutor scriptExecutor;
    public static LoggerManager loggerManager;
    public static SettingManager settingManager;
    public static EventManager eventManager;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitialize() {
        LOGGER.info("CubeCode Loaded!");

        EventHandler.init();
        Dispatcher.register();
    }
}
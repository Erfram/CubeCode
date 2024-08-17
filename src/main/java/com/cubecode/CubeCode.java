package com.cubecode;

import com.cubecode.api.events.EventManager;
import com.cubecode.network.Dispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import com.cubecode.api.scripts.ScriptManager;

import java.io.File;

public class CubeCode implements ModInitializer {
    public static final Logger LOGGER;

    public static final String MOD_ID;

    public static File cubeCodeDirectory;
    public static File factoryDirectory;
    public static File contentDirectory;

    public static ScriptManager scriptManager;
    public static EventManager eventManager;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitialize() {
        EventHandler.init();
        Dispatcher.register();
    }
}
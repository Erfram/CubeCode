package com.cubecode;

import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.FactoryManager;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import com.cubecode.api.scripts.ScriptManager;

public class CubeCode implements ModInitializer {

    public static final Logger LOGGER;

    public static final String MOD_ID;

    public static ScriptManager scriptManager;
    public static FactoryManager factoryManager;
    public static BlockManager blockManager;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitialize() {
        EventHandler.init();
    }
}
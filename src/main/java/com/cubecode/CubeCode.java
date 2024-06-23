package com.cubecode;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.WorldSavePath;
import org.slf4j.Logger;
import com.cubecode.api.scripts.ScriptManager;
import com.cubecode.content.CubeCodeCommand;
import com.cubecode.content.CubeCodeKeyBindings;

import java.io.File;

public class CubeCode implements ModInitializer {

    public static final Logger LOGGER;

    public static final String MOD_ID;

    public static ScriptManager scriptManager;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitialize() {
        CubeCodeKeyBindings.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CubeCodeCommand.init(dispatcher));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {

            File worldDirectory = server.getSavePath(WorldSavePath.ROOT).getParent().toFile();
            File cubeCodeDirectory = new File(worldDirectory, MOD_ID);

            if (cubeCodeDirectory.mkdirs()) {
                LOGGER.info(String.format("#### Creating a mod directory %s for world. ####", MOD_ID));
            }

            scriptManager = new ScriptManager(new File(cubeCodeDirectory, "scripts"));

        });
    }
}
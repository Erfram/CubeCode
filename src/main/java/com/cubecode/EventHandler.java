package com.cubecode;

import com.cubecode.api.scripts.ScriptManager;
import com.cubecode.content.CubeCodeCommand;
import com.cubecode.network.Dispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.WorldSavePath;

import java.io.File;

public class EventHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CubeCodeCommand.init(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            File worldDirectory = server.getSavePath(WorldSavePath.ROOT).getParent().toFile();

            CubeCode.cubeCodeDirectory = new File(worldDirectory, CubeCode.MOD_ID);
            CubeCode.factoryDirectory = new File(CubeCode.cubeCodeDirectory, "factory");
            CubeCode.contentDirectory = new File(CubeCode.factoryDirectory, "content");

            if (CubeCode.cubeCodeDirectory.mkdirs()) {
                CubeCode.LOGGER.info(String.format("#### Creating a mod directory %s for the world. ####", CubeCode.MOD_ID));
            }

            CubeCode.scriptManager = new ScriptManager(new File(CubeCode.cubeCodeDirectory, "scripts"));
        });
    }
}
package com.cubecode;

import com.cubecode.api.project.ProjectManager;
import com.cubecode.state.PlayerState;
import com.cubecode.state.ServerState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.WorldSavePath;

import java.io.File;

public class EventHandler {
    public static void init() {
        serverStarting();
        initCapability();
    }

    private static void serverStarting() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            File worldDirectory = server.getSavePath(WorldSavePath.ROOT).getParent().toFile();

            CubeCode.cubeCodeDirectory = new File(worldDirectory, CubeCode.MOD_ID);

            CubeCode.projectManager = new ProjectManager(new File(CubeCode.cubeCodeDirectory, "project"));

            if (CubeCode.cubeCodeDirectory.mkdirs()) {
                CubeCode.LOGGER.info("#### Creating a mod directory {} for the world. ####", CubeCode.MOD_ID);
            }
        });
    }

    private static void initCapability() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerState serverState = ServerState.getServerState(server);

            if (handler.player != null) {
                PlayerState playerState = ServerState.getPlayerState(handler.player);
                playerState.syncToClient();
            }

            serverState.markDirty();
        });
    }
}
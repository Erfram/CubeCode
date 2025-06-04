package com.cubecode;

import com.cubecode.state.PlayerState;
import com.cubecode.state.ServerState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.WorldSavePath;

import java.io.File;

public class EventHandler {
    public static void init() {
        //CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CubeCodeCommand.init(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            File worldDirectory = server.getSavePath(WorldSavePath.ROOT).getParent().toFile();

            CubeCode.cubeCodeDirectory = new File(worldDirectory, CubeCode.MOD_ID);
            CubeCode.factoryDirectory = new File(CubeCode.cubeCodeDirectory, "factory");
            CubeCode.contentDirectory = new File(CubeCode.factoryDirectory, "content");

            if (CubeCode.cubeCodeDirectory.mkdirs()) {
                CubeCode.LOGGER.info(String.format("#### Creating a mod directory %s for the world. ####", CubeCode.MOD_ID));
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerState serverState = ServerState.getServerState(server);

            if (handler.player != null) {
                PlayerState playerState = ServerState.getPlayerState(handler.player);
                playerState.syncToClient();
            }

            serverState.markDirty();
        });

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            NbtCompound nbt = new NbtCompound();
            if (world.isClient) {
//                nbt.putString("type", "client");
//                CubeCodeClient.playerState.setStates(nbt);
            } else {
                nbt.putString("type", "server");
                ServerState serverState = ServerState.getServerState(world.getServer());
                PlayerState playerState = ServerState.getPlayerState(player);
                playerState.setStates(nbt);

                serverState.markDirty();
                playerState.syncToClient();
            }

            return true;
        });
    }
}
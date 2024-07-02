package com.cubecode;

import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.scripts.ScriptManager;
import com.cubecode.content.CubeCodeCommand;
import com.cubecode.network.NetworkingPackets;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.WorldSavePath;

import java.io.File;
public class EventHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CubeCodeCommand.init(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            File worldDirectory = server.getSavePath(WorldSavePath.ROOT).getParent().toFile();
            File cubeCodeDirectory = new File(worldDirectory, CubeCode.MOD_ID);
            File factoryDirectory = new File(cubeCodeDirectory, "factory");

            if (cubeCodeDirectory.mkdirs()) {
                CubeCode.LOGGER.info(String.format("#### Creating a mod directory %s for the world. ####", CubeCode.MOD_ID));
            }

            CubeCode.scriptManager = new ScriptManager(new File(cubeCodeDirectory, "scripts"));
            CubeCode.factoryManager = new FactoryManager(new File(cubeCodeDirectory, "factory"));
            CubeCode.blockManager = new BlockManager(new File(factoryDirectory, "block"));

            CubeCode.blockManager.registerBlocks();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            FactoryManager.modifyingChunks(server);

            CubeCode.blockManager.unregisterBlocks();
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayNetworking.send(handler.player, NetworkingPackets.UNREGISTRIES_S2C_PACKET, PacketByteBufUtils.createBlockByteBuf());
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayNetworking.send(handler.player, NetworkingPackets.REGISTRIES_S2C_PACKET, PacketByteBufUtils.createBlockByteBuf());
        });
    }
}
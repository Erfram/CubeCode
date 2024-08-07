package com.cubecode;

import com.cubecode.api.factory.TextureManager;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.factory.effect.EffectManager;
import com.cubecode.api.factory.enchantment.EnchantmentManager;
import com.cubecode.api.factory.item.ItemManager;
import com.cubecode.api.factory.material.ToolMaterialManager;
import com.cubecode.api.factory.potion.PotionManager;
import com.cubecode.api.scripts.ScriptManager;
import com.cubecode.content.CubeCodeCommand;
import com.cubecode.network.NetworkingPackets;
import com.cubecode.utils.FactoryType;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.util.*;

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

            CubeCode.factoryManager = new FactoryManager(new File(CubeCode.cubeCodeDirectory, "factory"));
            CubeCode.textureManager = new TextureManager(new File(CubeCode.factoryDirectory, "textures"));

            CubeCode.blockManager = new BlockManager(new File(CubeCode.contentDirectory, "block"));
            CubeCode.itemManager = new ItemManager(new File(CubeCode.contentDirectory, "item"));
            CubeCode.toolMaterialManager = new ToolMaterialManager(new File(CubeCode.contentDirectory, "toolMaterial"));
            CubeCode.effectManager = new EffectManager(new File(CubeCode.contentDirectory, "effect"));
            CubeCode.potionManager = new PotionManager(new File(CubeCode.contentDirectory, "potion"));
            CubeCode.enchantmentManager = new EnchantmentManager(new File(CubeCode.contentDirectory, "enchantment"));

            CubeCode.factoryManagers = new LinkedHashMap<>();

            CubeCode.factoryManagers.put(FactoryType.BLOCK, CubeCode.blockManager);
            CubeCode.factoryManagers.put(FactoryType.ITEM, CubeCode.itemManager);
            CubeCode.factoryManagers.put(FactoryType.TOOL_MATERIAL, CubeCode.toolMaterialManager);
            CubeCode.factoryManagers.put(FactoryType.EFFECT, CubeCode.effectManager);
            CubeCode.factoryManagers.put(FactoryType.POTION, CubeCode.potionManager);
            CubeCode.factoryManagers.put(FactoryType.ENCHANTMENT, CubeCode.enchantmentManager);

            CubeCode.factoryManagers.forEach((key, value) -> value.register());
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            CubeCode.factoryManagers.forEach((key, value) -> value.updateElementToRemove());

            CubeCode.blockManager.modifyingChunks(server);

            CubeCode.factoryManagers.forEach((key, value) -> value.unregister());
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            CubeCode.factoryManagers.forEach((key, value) -> {
                List<List<String>> partitions = new ArrayList<>();
                List<String> elements = value.getElements();
                int partitionSize = 10;

                for (int i = 0; i < elements.size(); i += partitionSize) {
                    partitions.add(elements.subList(i, Math.min(i + partitionSize, elements.size())));
                }

                partitions.forEach(partitionElements -> {
                    sender.sendPacket(NetworkingPackets.REGISTRIES_S2C_PACKET, PacketByteBufUtils.createRegistryByteBuf(key, partitionElements));
                });
            });

            //sender.sendPacket(NetworkingPackets.REGISTRY_TEXTURE_S2C_PACKET, PacketByteBufUtils.createTextureByteBuf(CubeCode.textureManager.getFiles()));

            if (handler.player.server.isDedicated() || handler.player.server.isRemote()) {
                PacketByteBuf buf = PacketByteBufs.create();

                buf.writeInt(handler.player.getInventory().size());

                for (int i = 0; i < handler.player.getInventory().size(); i++) {
                    ItemStack stack = handler.player.getInventory().getStack(i);
                    buf.writeItemStack(stack);
                }

                sender.sendPacket(NetworkingPackets.SYNC_INVENTORY_S2C_PACKET, buf);
            }
        });
    }
}
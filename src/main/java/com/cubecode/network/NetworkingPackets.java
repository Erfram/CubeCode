package com.cubecode.network;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.block.ExampleBlock;
import com.cubecode.api.utils.GSONManager;
import com.cubecode.utils.CubeRegistry;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class NetworkingPackets {
    public static final Identifier REGISTRIES_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "registries_s2c_packet");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(REGISTRIES_S2C_PACKET, (client, handler, buf, responseSender) -> {
            String[] blocks = PacketByteBufUtils.readStringArray(buf);
            client.execute(() -> {
                CubeRegistry<?> blockRegistry = (CubeRegistry<?>) Registries.BLOCK;
                CubeRegistry<?> itemRegistry = (CubeRegistry<?>) Registries.ITEM;

                blockRegistry.unFreeze();
                itemRegistry.unFreeze();

                for (String jsonBlock : blocks) {
                    BlockManager.DefaultBlock defaultBlock = GSONManager.readJSON(jsonBlock, BlockManager.DefaultBlock.class);

                    FabricBlockSettings blockSettings = FabricBlockSettings.create();
                    blockSettings.hardness(defaultBlock.hardness);

                    ExampleBlock block = new ExampleBlock(blockSettings);

                    FactoryManager.registerBlock(blockRegistry, defaultBlock.id, block);

                    if (defaultBlock.isCreateItemBlock) {
                        FactoryManager.registerBlockItem(itemRegistry, defaultBlock.id, block);
                    }
                }

                blockRegistry.freeze();
                itemRegistry.freeze();
            });
        });
    }
}

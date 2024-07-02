package com.cubecode.network;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkingPackets {
    public static final Identifier REGISTRIES_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "registries_s2c_packet");
    public static final Identifier UNREGISTRIES_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "unregistries_s2c_packet");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(REGISTRIES_S2C_PACKET, (client, handler, buf, responseSender) -> {
            String[] blocks = PacketByteBufUtils.readStringArray(buf);
            client.execute(() -> {
                BlockManager.registerBlocksClient(blocks);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(UNREGISTRIES_S2C_PACKET, (client, handler, buf, responseSender) -> {
            String[] blocks = PacketByteBufUtils.readStringArray(buf);
            client.execute(() -> {
                BlockManager.unregisterBlocksClient(blocks);
            });
        });
    }
}

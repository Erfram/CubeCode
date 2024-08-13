package com.cubecode.network.basic;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public interface ClientPacketHandler<T extends AbstractPacket> extends PacketHandler {
    void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, T packet);
}

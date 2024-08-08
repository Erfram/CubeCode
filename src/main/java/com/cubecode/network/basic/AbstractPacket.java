package com.cubecode.network.basic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public abstract class AbstractPacket {
    public final PacketByteBuf buf = PacketByteBufs.create();

    public abstract void fromBytes(PacketByteBuf buf);

    public abstract void toBytes(PacketByteBuf buf);

    public abstract Identifier getIdentifier();

    @Environment(EnvType.CLIENT)
    public void client(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {

    }

    @Environment(EnvType.SERVER)
    public void server(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender) {
    }
}
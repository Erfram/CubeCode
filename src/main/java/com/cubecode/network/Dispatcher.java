package com.cubecode.network;

import com.cubecode.network.basic.AbstractDispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.packets.all.*;
import com.cubecode.network.packets.all.CreateScriptPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Dispatcher {
    private static final AbstractDispatcher DISPATCHER = new AbstractDispatcher() {
        @Override
        public void register() {
            /* Scripting system */
            this.registerPacket(IDENodeRequestPacket.class, IDENodeRequestPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(IDENodeRequestPacket.class, IDENodeRequestPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(IDESyncPacket.class, IDESyncPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(IDESyncPacket.class, IDESyncPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(CreateScriptPacket.class, CreateScriptPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(CreateScriptPacket.class, CreateScriptPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(ScriptRunC2SPacket.class, ScriptRunC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(ScriptExecutionResultPacket.class, ScriptExecutionResultPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(ScriptSaveC2SPacket.class, ScriptSaveC2SPacket.ServerHandler.class, EnvType.SERVER);

            /* Events */
            this.registerPacket(EventsRequestedPacket.class, EventsRequestedPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(EventsRequestedPacket.class, EventsRequestedPacket.ServerHandler.class, EnvType.SERVER);

            /* States */
            this.registerPacket(StatesRequestedPacket.class, StatesRequestedPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(StatesRequestedPacket.class, StatesRequestedPacket.ServerHandler.class, EnvType.SERVER);
        }
    };

    public static void sendTo(AbstractPacket packet, ServerPlayerEntity player) {
        PacketByteBuf buf = packet.buf;
        packet.toBytes(buf);
        ServerPlayNetworking.send(player, packet.getIdentifier(), buf);
    }

    public static void sendToAll(AbstractPacket packet, MinecraftServer server) {
        PacketByteBuf buf = packet.buf;
        packet.toBytes(buf);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, packet.getIdentifier(), buf);
        }
    }

    public static void sendToServer(AbstractPacket packet) {
        PacketByteBuf buf = packet.buf;
        packet.toBytes(buf);
        ClientPlayNetworking.send(packet.getIdentifier(), buf);
    }

    public static void register() {
        DISPATCHER.register();
    }
}

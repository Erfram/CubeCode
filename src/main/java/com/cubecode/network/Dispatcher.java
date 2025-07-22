package com.cubecode.network;

import com.cubecode.network.basic.AbstractDispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.packets.all.IDERequestedPacket;
import com.cubecode.network.packets.all.IDEUpdatePacket;
import com.cubecode.network.packets.all.PlayerStateSyncPacket;
import com.cubecode.network.packets.server.CodeRunC2SPacket;
import com.cubecode.network.packets.server.FileCreateC2SPacket;
import com.cubecode.network.packets.server.ScriptRunC2SPacket;
import com.cubecode.network.packets.server.ScriptSaveC2SPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class Dispatcher {
    private static final AbstractDispatcher DISPATCHER = new AbstractDispatcher() {
        @Override
        public void register() {
            //ALL
            registerPacket(PlayerStateSyncPacket.class, PlayerStateSyncPacket.ServerHandler.class, EnvType.SERVER);
            registerPacket(PlayerStateSyncPacket.class, PlayerStateSyncPacket.ClientHandler.class, EnvType.CLIENT);
            registerPacket(IDERequestedPacket.class, IDERequestedPacket.ServerHandler.class, EnvType.SERVER);
            registerPacket(IDERequestedPacket.class, IDERequestedPacket.ClientHandler.class, EnvType.CLIENT);
            registerPacket(IDEUpdatePacket.class, IDEUpdatePacket.ServerHandler.class, EnvType.SERVER);
            registerPacket(IDEUpdatePacket.class, IDEUpdatePacket.ClientHandler.class, EnvType.CLIENT);

            //SERVER
            registerPacket(ScriptRunC2SPacket.class, ScriptRunC2SPacket.ServerHandler.class, EnvType.SERVER);
            registerPacket(CodeRunC2SPacket.class, CodeRunC2SPacket.ServerHandler.class, EnvType.SERVER);
            registerPacket(ScriptSaveC2SPacket.class, ScriptSaveC2SPacket.ServerHandler.class, EnvType.SERVER);
            registerPacket(FileCreateC2SPacket.class, FileCreateC2SPacket.ServerHandler.class, EnvType.SERVER);

            //CLIENT
        }
    };

    public static void sendTo(AbstractPacket packet, ServerPlayerEntity player) {
        PacketByteBuf buf = packet.buf;
        packet.toBytes(buf);
        ServerPlayNetworking.send(player, packet.getIdentifier(), buf);
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

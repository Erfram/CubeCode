package com.cubecode.network;

import com.cubecode.network.basic.AbstractDispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.packets.client.UpdateScriptsS2CPacket;
import com.cubecode.network.packets.server.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class Dispatcher {
    private static final AbstractDispatcher DISPATCHER = new AbstractDispatcher() {
        @Override
        public void register() {
            this.registerPacket(UpdateScriptsS2CPacket.class, UpdateScriptsS2CPacket.ClientHandler.class, EnvType.CLIENT);

            this.registerPacket(UpdateScriptsC2SPacket.class, UpdateScriptsC2SPacket.ServerPacketHandler.class, EnvType.SERVER);
            this.registerPacket(CreateScriptC2SPacket.class, CreateScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(RenameScriptC2SPacket.class, RenameScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(DeleteScriptC2SPacket.class, DeleteScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(RunScriptC2SPacket.class, RunScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(SaveScriptC2SPacket.class, SaveScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(RequestEventsC2SPacket.class, RequestEventsC2SPacket.ServerHandler.class, EnvType.SERVER);
        }
    };

    public static void sendTo(AbstractPacket packet, ServerPlayerEntity player) {
        PacketByteBuf buf = packet.buf;
        packet.fromBytes(buf);
        ServerPlayNetworking.send(player, packet.getIdentifier(), buf);
    }

    public static void sendToServer(AbstractPacket packet) {
        PacketByteBuf buf = packet.buf;
        packet.fromBytes(buf);
        ClientPlayNetworking.send(packet.getIdentifier(), buf);
    }

    public static void register() {
        DISPATCHER.register();
    }
}

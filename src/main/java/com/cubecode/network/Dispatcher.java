package com.cubecode.network;

import com.cubecode.network.basic.AbstractDispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.packets.server.CreateFolderC2SPacket;
import com.cubecode.network.packets.server.CreateScriptC2SPacket;
import com.cubecode.network.packets.all.EventsRequestedPacket;
import com.cubecode.network.packets.all.IDEARequestedPacket;
import com.cubecode.network.packets.client.FillScriptScopeS2CPacket;
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
            this.registerPacket(EventsRequestedPacket.class, EventsRequestedPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(FillScriptScopeS2CPacket.class, FillScriptScopeS2CPacket.ClientHandler.class, EnvType.CLIENT);
            this.registerPacket(IDEARequestedPacket.class, IDEARequestedPacket.ClientHandler.class, EnvType.CLIENT);

            this.registerPacket(UpdateScriptsC2SPacket.class, UpdateScriptsC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(CreateScriptC2SPacket.class, CreateScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(CreateFolderC2SPacket.class, CreateFolderC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(DeleteElementC2SPacket.class, DeleteElementC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(InsertElementC2SPacket.class, InsertElementC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(RenameElementC2SPacket.class, RenameElementC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(RunScriptC2SPacket.class, RunScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(SaveScriptC2SPacket.class, SaveScriptC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(EventsSyncC2SPacket.class, EventsSyncC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(EventsRequestedPacket.class, EventsRequestedPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(RequestScriptScopeC2SPacket.class, RequestScriptScopeC2SPacket.ServerHandler.class, EnvType.SERVER);
            this.registerPacket(IDEARequestedPacket.class, IDEARequestedPacket.ServerHandler.class, EnvType.SERVER);
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

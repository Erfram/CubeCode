package com.cubecode.network.packets.server;

import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.state.ServerState;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RequestEventsC2SPacket extends AbstractPacket {
    NbtList events;

    public RequestEventsC2SPacket(NbtList events) {
        this.events = events;
    }

    public RequestEventsC2SPacket() {}

    @Override
    public void fromBytes(PacketByteBuf buf) {
        buf.writeNbt(events);
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        events = (NbtList) buf.readNbt(NbtSizeTracker.ofUnlimitedBytes());
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("request_player_state_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RequestEventsC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RequestEventsC2SPacket packet) {
            ServerState serverState = ServerState.getServerState(server);

            serverState.events = packet.events;
            serverState.markDirty();
        }
    }
}
package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.state.PlayerState;
import com.cubecode.state.ServerState;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerStateSyncPacket extends AbstractPacket {
    NbtCompound nbt;

    public PlayerStateSyncPacket() {}

    public PlayerStateSyncPacket(PlayerState playerState) {
        this.nbt = playerState.serialize();
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeNbt(this.nbt);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.nbt = buf.readNbt();
    }

    @Override
    public Identifier getIdentifier() {
        return CubeCode.createId("player_state_sync");
    }

    public static class ServerHandler implements ServerPacketHandler<PlayerStateSyncPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, PlayerStateSyncPacket packet) {
            ServerState.getPlayerState(player).deserialize(packet.nbt);
            ServerState.getServerState(server).markDirty();
        }
    }

    public static class ClientHandler implements ClientPacketHandler<PlayerStateSyncPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, PlayerStateSyncPacket packet) {
            CubeCodeClient.playerState.deserialize(packet.nbt);
        }
    }
}

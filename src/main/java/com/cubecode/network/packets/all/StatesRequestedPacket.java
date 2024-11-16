package com.cubecode.network.packets.all;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.views.StatesView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
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

import java.util.ArrayList;
import java.util.List;

public class StatesRequestedPacket extends AbstractPacket {
    NbtCompound nbt;
    List<String> playerNames = new ArrayList<>();

    public StatesRequestedPacket() {

    }

    public StatesRequestedPacket(NbtCompound nbt, List<String> playerNames) {
        this.nbt = nbt;
        this.playerNames = playerNames;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeNbt(this.nbt);
        buf.writeCollection(this.playerNames, PacketByteBuf::writeString);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.nbt = buf.readNbt();
        this.playerNames = buf.readList(PacketByteBuf::readString);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("cubecode_states_requested");
    }

    public static class ServerHandler implements ServerPacketHandler<StatesRequestedPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, StatesRequestedPacket packet) {
            NbtCompound nbt = new NbtCompound();

            nbt.put("server", ServerState.getServerState(server).values);

            for (String playerName : server.getPlayerNames()) {
                nbt.put(playerName, ServerState.getPlayerState(server.getPlayerManager().getPlayer(playerName)).getValues());
            }

            Dispatcher.sendTo(new StatesRequestedPacket(nbt, new ArrayList<>(List.of(server.getPlayerManager().getPlayerNames()))), player);
        }
    }

    public static class ClientHandler implements ClientPacketHandler<StatesRequestedPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, StatesRequestedPacket packet) {
            ImGuiLoader.pushView(new StatesView(packet.nbt, packet.playerNames));
        }
    }
}

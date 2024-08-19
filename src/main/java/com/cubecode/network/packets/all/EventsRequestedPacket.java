package com.cubecode.network.packets.all;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.views.EventView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.network.packets.server.UpdateScriptsC2SPacket;
import com.cubecode.state.ServerState;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EventsRequestedPacket extends AbstractPacket {
    NbtList events = null;

    public EventsRequestedPacket() {}

    public EventsRequestedPacket(NbtList events) {
        this.events = events;
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        buf.writeBoolean(events != null);
        if (events != null) {
            buf.writeNbt(events);
        }
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        if (buf.readBoolean()) {
            events = (NbtList) buf.readNbt(NbtSizeTracker.ofUnlimitedBytes());
        }
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("events_requested_packet");
    }

    public static class ServerHandler implements ServerPacketHandler<EventsRequestedPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, EventsRequestedPacket packet) {
            Dispatcher.sendTo(new EventsRequestedPacket(ServerState.getServerState(server).events), player);
        }
    }

    public static class ClientHandler implements ClientPacketHandler<EventsRequestedPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, EventsRequestedPacket packet) {
            NbtList events = packet.events;

            ImGuiLoader.pushView(new EventView(events));
        }
    }
}

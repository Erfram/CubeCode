package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.api.events.EventManager;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.views.EventsView;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.state.ServerState;
import com.cubecode.utils.PacketByteBufUtils;
import com.cubecode.api.scripts.Script;
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

import java.util.ArrayList;
import java.util.List;

public class EventsRequestedPacket extends AbstractPacket {
    NbtList usedEvents = null;
    NbtList events = null;
    List<Script> scripts = new ArrayList<>();

    public EventsRequestedPacket() {}

    public EventsRequestedPacket(NbtList usedEvents, NbtList events, List<Script> scripts) {
        this.usedEvents = usedEvents;
        this.events = events;
        this.scripts = new ArrayList<>(scripts);
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeBoolean(this.usedEvents != null);
        if (this.usedEvents != null) {
            buf.writeNbt(usedEvents);
        }
        buf.writeBoolean(this.events != null);
        if (this.events != null) {
            buf.writeNbt(this.events);
        }
        buf.writeCollection(
                this.scripts,
                PacketByteBufUtils::writeScript
        );
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        if (buf.readBoolean()) {
            this.usedEvents = (NbtList) buf.readNbt(NbtSizeTracker.ofUnlimitedBytes());
        }
        if (buf.readBoolean()) {
            this.events = (NbtList) buf.readNbt(NbtSizeTracker.ofUnlimitedBytes());
        }
        //this.scripts = buf.readList(PacketByteBufUtils::readScript);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("events_requested_packet");
    }

    public static class ServerHandler implements ServerPacketHandler<EventsRequestedPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, EventsRequestedPacket packet) {
            Dispatcher.sendTo(new EventsRequestedPacket(
                    ServerState.getServerState(server).events,
                    EventManager.cubeEventsToNbtList(CubeCode.eventManager.events),
                    CubeCode.scriptManager.getScripts()
            ), player);
        }
    }

    public static class ClientHandler implements ClientPacketHandler<EventsRequestedPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, EventsRequestedPacket packet) {
            NbtList usedEvents = packet.usedEvents;
            NbtList events = packet.events;
            List<Script> scripts = packet.scripts;

            ImGuiLoader.pushView(new EventsView(EventManager.nbtListToCubeEvents(events), scripts));
        }
    }
}

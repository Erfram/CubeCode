package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.views.ide.core.CubeCodeIDEView;
import com.cubecode.client.views.ide.utils.node.IdeaNode;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class IDESyncPacket extends AbstractPacket {
    List<IdeaNode> nodes = new ArrayList<>();

    public IDESyncPacket() {

    }

    public IDESyncPacket(List<IdeaNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeCollection(this.nodes, PacketByteBufUtils::writeIdeaNode);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.nodes = buf.readList(PacketByteBufUtils::readIdeaNode);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("cubecode", "ide_sync_packet");
    }

    public static class ServerHandler implements ServerPacketHandler<IDESyncPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, IDESyncPacket packet) {
            Dispatcher.sendTo(new IDESyncPacket(CubeCode.projectManager.getNodes()), player);
        }
    }

    public static class ClientHandler implements ClientPacketHandler<IDESyncPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, IDESyncPacket packet) {
            ImGuiLoader.getViews(CubeCodeIDEView.class).forEach(view -> {
                view.nodes = new CopyOnWriteArrayList<>(packet.nodes);
            });
        }
    }
}

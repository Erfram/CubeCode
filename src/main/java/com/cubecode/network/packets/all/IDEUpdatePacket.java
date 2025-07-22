package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.api.project.nodes.IDENode;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.views.ide.IDEView;
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

public class IDEUpdatePacket extends AbstractPacket {
    List<IDENode> nodes;

    public IDEUpdatePacket() {
        this.nodes = new ArrayList<>();
    }

    public IDEUpdatePacket(List<IDENode> nodes) {
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
        return CubeCode.createId("update_ide");
    }

    public static class ClientHandler implements ClientPacketHandler<IDEUpdatePacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, IDEUpdatePacket packet) {
            for (IDEView view : ImGuiLoader.getViews(IDEView.class)) {
                view.update(packet.nodes);
            }
        }
    }

    public static class ServerHandler implements ServerPacketHandler<IDEUpdatePacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, IDEUpdatePacket packet) {
            Dispatcher.sendTo(new IDEUpdatePacket(CubeCode.projectManager.getNodes()), player);
        }
    }
}

package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.client.views.idea.utils.node.NodeType;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DeleteElementC2SPacket extends AbstractPacket {
    String path;
    NodeType nodeType;

    public DeleteElementC2SPacket() {

    }

    public DeleteElementC2SPacket(String path, NodeType nodeType) {
        this.path = path;
        this.nodeType = nodeType;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(path);
        buf.writeInt(nodeType.ordinal());
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        path = buf.readString();
        nodeType = NodeType.values()[buf.readInt()];
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("delete_element_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<DeleteElementC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, DeleteElementC2SPacket packet) {
            CubeCode.projectManager.deleteFile(packet.path.substring(1), packet.nodeType);
            CubeCode.projectManager.updateIdeaNodesFromFiles();
            CubeCode.projectManager.updateScriptsFromFiles();
        }
    }
}

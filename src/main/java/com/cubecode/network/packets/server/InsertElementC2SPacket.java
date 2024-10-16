package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.client.views.idea.utils.node.FolderNode;
import com.cubecode.client.views.idea.utils.node.IdeaNode;
import com.cubecode.client.views.idea.utils.node.ScriptNode;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InsertElementC2SPacket extends AbstractPacket {
    IdeaNode ideaNode;
    String path;

    public InsertElementC2SPacket() {
    }

    public InsertElementC2SPacket(IdeaNode ideaNode, String path) {
        this.ideaNode = ideaNode;
        this.path = path;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeIdeaNode(buf, ideaNode);
        buf.writeString(path);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        ideaNode = PacketByteBufUtils.readIdeaNode(buf);
        path = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("insert_element_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<InsertElementC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, InsertElementC2SPacket packet) {
            switch (packet.ideaNode.getType()) {
                case FOLDER -> {
                    CubeCode.projectManager.createFolderAndScripts((FolderNode) packet.ideaNode, packet.path);

                    CubeCode.projectManager.updateIdeaNodesFromFiles();
                }
                case SCRIPT -> {
                    CubeCode.projectManager.createScript(packet.ideaNode.getName(), packet.path, ((ScriptNode)packet.ideaNode).getScript().code);
                    CubeCode.projectManager.updateScriptsFromFiles();
                }
            }
        }
    }
}

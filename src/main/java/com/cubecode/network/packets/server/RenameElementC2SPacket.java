package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.client.views.ide.utils.node.NodeType;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RenameElementC2SPacket extends AbstractPacket {
    String path;
    String name;
    NodeType type;

    public RenameElementC2SPacket() {

    }

    public RenameElementC2SPacket(String path, String name, NodeType type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(path);
        buf.writeString(name);
        buf.writeEnumConstant(type);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        path = buf.readString();
        name = buf.readString();
        type = buf.readEnumConstant(NodeType.class);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("rename_element_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RenameElementC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RenameElementC2SPacket packet) {
            CubeCode.projectManager.renameFile(packet.path.substring(1), packet.name);

            switch (packet.type) {
                case SCRIPT -> CubeCode.settingManager.renameScriptName(packet.path.substring(1), packet.name);
                case FOLDER -> CubeCode.settingManager.renameFolderName(packet.path.substring(1), packet.name);
            }
        }
    }
}

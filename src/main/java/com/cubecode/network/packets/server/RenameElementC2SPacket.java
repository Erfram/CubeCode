package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
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

    public RenameElementC2SPacket() {

    }

    public RenameElementC2SPacket(String path, String name) {
        this.path = path;
        this.name = name;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(path);
        buf.writeString(name);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        path = buf.readString();
        name = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("rename_element_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RenameElementC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RenameElementC2SPacket packet) {
            CubeCode.projectManager.renameFile(packet.path.substring(1), packet.name);
            CubeCode.projectManager.updateIdeaNodesFromFiles();
            CubeCode.projectManager.updateScriptsFromFiles();
        }
    }
}

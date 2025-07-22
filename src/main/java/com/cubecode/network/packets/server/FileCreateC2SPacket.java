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

public class FileCreateC2SPacket extends AbstractPacket {
    String path;
    boolean isFolder;

    public FileCreateC2SPacket() {

    }

    public FileCreateC2SPacket(String path, boolean isFolder) {
        this.path = path;
        this.isFolder = isFolder;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.path);
        buf.writeBoolean(this.isFolder);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.path = buf.readString();
        this.isFolder = buf.readBoolean();
    }

    @Override
    public Identifier getIdentifier() {
        return CubeCode.createId("file_create_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<FileCreateC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, FileCreateC2SPacket packet) {
            if (packet.isFolder) {
                CubeCode.projectManager.createFolder(packet.path);
            } else {
                CubeCode.projectManager.createScript(packet.path);
            }
        }
    }
}

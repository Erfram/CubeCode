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

public class CreateFolderC2SPacket extends AbstractPacket {
    String folderName;
    String folderPath;

    public CreateFolderC2SPacket() {

    }

    public CreateFolderC2SPacket(String folderName, String folderPath) {
        this.folderName = folderName;
        this.folderPath = folderPath;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.folderName);
        buf.writeString(this.folderPath);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.folderName = buf.readString();
        this.folderPath = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("create_folder_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<CreateFolderC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, CreateFolderC2SPacket packet) {
            CubeCode.projectManager.createFolder(packet.folderName, packet.folderPath);

            CubeCode.projectManager.updateIdeaNodesFromFiles();
        }
    }
}
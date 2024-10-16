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

public class UpdateScriptsC2SPacket extends AbstractPacket {
    boolean isUpdate;

    public UpdateScriptsC2SPacket(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public UpdateScriptsC2SPacket() {}

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeBoolean(this.isUpdate);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.isUpdate = buf.readBoolean();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "update_scripts_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<UpdateScriptsC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, UpdateScriptsC2SPacket packet) {
            if (packet.isUpdate) {
                CubeCode.projectManager.updateScriptsFromFiles();
            }
        }
    }
}
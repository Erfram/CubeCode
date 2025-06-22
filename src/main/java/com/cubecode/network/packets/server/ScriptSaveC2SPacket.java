package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ScriptSaveC2SPacket extends AbstractPacket {
    String path;
    String code;

    public ScriptSaveC2SPacket() {}

    public ScriptSaveC2SPacket(String path, String code) {
        this.path = path;
        this.code = code;
    }

    public ScriptSaveC2SPacket(ScriptNode node) {
        this.path = node.getPath();
        this.code = node.getScript().getCode();
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.path);
        buf.writeString(this.code);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.path = buf.readString();
        this.code = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return CubeCode.createId("script_save_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<ScriptSaveC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, ScriptSaveC2SPacket packet) {
            CubeCode.projectManager.setScriptCode(packet.path.substring(1), packet.code);
        }
    }
}

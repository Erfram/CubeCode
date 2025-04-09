package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Script;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ScriptSaveC2SPacket extends AbstractPacket {
    Script script;
    String relativePath;

    public ScriptSaveC2SPacket() {

    }

    public ScriptSaveC2SPacket(Script script, String relativePath) {
        this.script = script;
        this.relativePath = relativePath;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeScript(buf, script);
        buf.writeString(relativePath);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.script = PacketByteBufUtils.readScript(buf);
        this.relativePath = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("cubecode", "script_save");
    }

    public static class ServerHandler implements ServerPacketHandler<ScriptSaveC2SPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, ScriptSaveC2SPacket packet) {
            CubeCode.scriptManager.putScript(packet.script);
            CubeCode.scriptManager.saveScript(packet.script, packet.relativePath);
        }
    }
}

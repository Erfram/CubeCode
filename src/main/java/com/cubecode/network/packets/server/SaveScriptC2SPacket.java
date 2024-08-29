package com.cubecode.network.packets.server;

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

public class SaveScriptC2SPacket extends AbstractPacket {
    public Script script;

    public SaveScriptC2SPacket(Script script) {
        this.script = script;
    }

    public SaveScriptC2SPacket() {

    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeScript(buf, script);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        script = PacketByteBufUtils.readScript(buf);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "save_script_c2s_packet");
    }

    public static class ServerHandler implements ServerPacketHandler<SaveScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, SaveScriptC2SPacket packet) {
            CubeCode.scriptManager.saveScript(packet.script);
        }
    }
}

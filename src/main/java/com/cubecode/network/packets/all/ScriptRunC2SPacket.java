package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.Script;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.scripting.ScriptExecutionResult;
import com.cubecode.utils.PacketByteBufUtils;
import com.cubecode.utils.TextUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ScriptRunC2SPacket extends AbstractPacket {
    Script script;

    public ScriptRunC2SPacket() {

    }

    public ScriptRunC2SPacket(Script script) {
        this.script = script;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeScript(buf, this.script);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.script = PacketByteBufUtils.readScript(buf);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("cubecode", "script_run");
    }

    public static class ServerHandler implements ServerPacketHandler<ScriptRunC2SPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, ScriptRunC2SPacket packet) {
            Script script = CubeCode.scriptManager.getScript(packet.script.getUUID());

            Properties properties = Properties.create(script.getName(), "server", player, null, player.getWorld(), server);

            ScriptExecutionResult result = script.run(script.getName(), properties);

            if (result.error) {
                player.sendMessage(TextUtils.formatText(result.errorMessage).withColor(0xe3256b));
            }
        }
    }
}

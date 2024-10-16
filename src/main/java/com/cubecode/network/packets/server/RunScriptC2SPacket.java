package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.api.scripts.code.ScriptServer;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class RunScriptC2SPacket extends AbstractPacket {
    Script script;

    public RunScriptC2SPacket(Script script) {
        this.script = script;
    }

    public RunScriptC2SPacket() {

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
        return new Identifier(CubeCode.MOD_ID, "run_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RunScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RunScriptC2SPacket packet) {

            Properties properties = Properties.create(
                    null,
                    null,
                    player,
                    null,
                    player.getWorld(),
                    server
            );

            try {
                packet.script.run(packet.script.name, properties);
            } catch (CubeCodeException cce) {
                player.sendMessage(MutableText.of(Text.of(cce.getMessage()).getContent()).formatted(Formatting.RED));
                cce.printStackTrace();
            }
        }
    }
}

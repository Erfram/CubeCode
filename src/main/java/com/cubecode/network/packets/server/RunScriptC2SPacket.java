package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Properties;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.network.packets.all.IDESyncPacket;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.PacketByteBufUtils;
import com.cubecode.api.scripts.Script;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
        PacketByteBufUtils.writeScript(buf, this.script);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.script = PacketByteBufUtils.readScript(buf);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "run_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RunScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RunScriptC2SPacket packet) {
            Properties properties = Properties.create(
                    packet.script.getName(),
                    "main",
                    player,
                    null,
                    player.getWorld(),
                    server
            );

            try {
                Script script = CubeCode.projectManager.getScript(packet.script.getName());
                script.run(script.getName(), properties);
            } catch (CubeCodeException cce) {
                player.sendMessage(Text.of("§c" + cce.getMessage()));
                cce.printStackTrace();
            } finally {
                Dispatcher.sendTo(new IDESyncPacket(CubeCode.projectManager.getNodes()), player);
            }
        }
    }
}

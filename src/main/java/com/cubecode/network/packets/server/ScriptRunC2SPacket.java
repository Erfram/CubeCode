package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.api.project.scripts.Properties;
import com.cubecode.api.project.scripts.Script;
import com.cubecode.exceptions.CubeCodeException;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScriptRunC2SPacket extends AbstractPacket {
    ScriptNode scriptNode;

    public ScriptRunC2SPacket() {}

    public ScriptRunC2SPacket(ScriptNode scriptNode) {
        this.scriptNode = scriptNode;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeIdeaNode(buf, this.scriptNode);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scriptNode = (ScriptNode) PacketByteBufUtils.readIdeaNode(buf);
    }

    @Override
    public Identifier getIdentifier() {
        return CubeCode.createId("script_run_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<ScriptRunC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, ScriptRunC2SPacket packet) {
            String name = packet.scriptNode.getName();
            Properties properties = Properties.create(
                    name,
                    "main",
                    player,
                    null,
                    player.getWorld(),
                    server
            );

            try {
                Script script = CubeCode.projectManager.getScript(name);
                script.run(name, properties);
            } catch (CubeCodeException cce) {
                player.sendMessage(Text.of("§c" + cce.getMessage()));
                cce.printStackTrace();
            } finally {
                //Dispatcher.sendTo(new IDESyncPacket(CubeCode.projectManager.getNodes()), player);
            }
        }
    }
}

package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.views.idea.utils.node.ScriptNode;
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
    public ScriptNode scriptNode;

    public SaveScriptC2SPacket(ScriptNode scriptNode) {
        this.scriptNode = scriptNode;
    }

    public SaveScriptC2SPacket() {

    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeIdeaNode(buf, scriptNode);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        scriptNode = (ScriptNode) PacketByteBufUtils.readIdeaNode(buf);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "save_script_c2s_packet");
    }

    public static class ServerHandler implements ServerPacketHandler<SaveScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, SaveScriptC2SPacket packet) {
            CubeCode.projectManager.saveScript(packet.scriptNode);
            CubeCode.projectManager.updateIdeaNodesFromFiles();
            CubeCode.projectManager.updateScriptsFromFiles();
        }
    }
}

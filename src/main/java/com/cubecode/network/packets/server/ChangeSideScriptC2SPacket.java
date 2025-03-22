package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.client.views.ide.utils.node.ScriptNode;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import com.cubecode.utils.ScriptType;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ChangeSideScriptC2SPacket extends AbstractPacket {
    ScriptNode node;
    ScriptType side;

    public ChangeSideScriptC2SPacket() {

    }

    public ChangeSideScriptC2SPacket(ScriptNode node, ScriptType side) {
        this.node = node;
        this.side = side;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeIdeaNode(buf, this.node);
        buf.writeEnumConstant(this.side);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.node = (ScriptNode) PacketByteBufUtils.readIdeaNode(buf);
        this.side = buf.readEnumConstant(ScriptType.class);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("cubecode_chage_side_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<ChangeSideScriptC2SPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, ChangeSideScriptC2SPacket packet) {
            CubeCode.settingManager.setScriptSide(packet.node.getScript().getName(), packet.side);
        }
    }
}

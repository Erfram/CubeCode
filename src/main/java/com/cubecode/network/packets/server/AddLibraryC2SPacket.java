package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.client.views.ide.utils.node.ScriptNode;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AddLibraryC2SPacket extends AbstractPacket {
    ScriptNode scriptNode;
    String library;

    public AddLibraryC2SPacket() {

    }

    public AddLibraryC2SPacket(ScriptNode scriptNode, String library) {
        this.scriptNode = scriptNode;
        this.library = library;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeIdeaNode(buf, this.scriptNode);
        buf.writeString(this.library);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scriptNode = (ScriptNode) PacketByteBufUtils.readIdeaNode(buf);
        this.library = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("cubecode_add_library_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<AddLibraryC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, AddLibraryC2SPacket packet) {
            CubeCode.projectManager.addLibraryScript(packet.scriptNode.getScript().getName(), packet.library);
            CubeCode.settingManager.addLibrary(packet.scriptNode.getScript().getName(), packet.library);
        }
    }
}

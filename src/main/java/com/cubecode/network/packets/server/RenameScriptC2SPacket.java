package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.network.packets.client.UpdateScriptsS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class RenameScriptC2SPacket extends AbstractPacket {
    String oldName;
    String newName;

    public RenameScriptC2SPacket(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public RenameScriptC2SPacket() {
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.oldName);
        buf.writeString(this.newName);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.oldName = buf.readString();
        this.newName = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "rename_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RenameScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RenameScriptC2SPacket packet) {
            String newName = packet.newName.endsWith(".js") ? packet.newName : packet.newName + ".js";

            CubeCode.scriptManager.renameFile(packet.oldName, newName);

            CubeCode.scriptManager.updateScriptsFromFiles();

            Dispatcher.sendTo(new UpdateScriptsS2CPacket(CubeCode.scriptManager.getScripts()), player);
        }
    }
}

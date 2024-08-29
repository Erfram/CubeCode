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

public class DeleteScriptC2SPacket extends AbstractPacket {
    String scriptName;

    public DeleteScriptC2SPacket(String scriptName) {
        this.scriptName = scriptName;
    }

    public DeleteScriptC2SPacket() {}

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(scriptName);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        scriptName = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "delete_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<DeleteScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, DeleteScriptC2SPacket packet) {
            CubeCode.scriptManager.deleteScript(packet.scriptName);
            CubeCode.scriptManager.updateScriptsFromFiles();

            Dispatcher.sendTo(new UpdateScriptsS2CPacket(), player);
        }
    }
}

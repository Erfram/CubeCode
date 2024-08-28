package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
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

public class CreateScriptC2SPacket extends AbstractPacket {
    String scriptName;

    public CreateScriptC2SPacket(String scriptName) {
        this.scriptName = scriptName;
    }

    public CreateScriptC2SPacket() {

    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.scriptName);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scriptName = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "create_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<CreateScriptC2SPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, CreateScriptC2SPacket packet) {
            String code = CubeCodeConfig.getScriptConfig().contextName + ".server.send(\"Hellow World!\", false)";

            String scriptName = packet.scriptName.endsWith(".js") ? packet.scriptName : packet.scriptName + ".js";

            CubeCode.scriptManager.createScript(scriptName, code);
            CubeCode.scriptManager.updateScriptsFromFiles();
            Dispatcher.sendToServer(new UpdateScriptsS2CPacket(CubeCode.scriptManager.getScripts()));
        }
    }
}

package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.ScriptExecutor;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.api.scripts.Script;
import com.cubecode.utils.ScriptType;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CreateScriptPacket extends AbstractPacket {
    String scriptName;
    String scriptPath;
    ScriptType scriptType;

    public CreateScriptPacket(String scriptName, String scriptPath, ScriptType scriptType) {
        this.scriptName = scriptName;
        this.scriptPath = scriptPath;
        this.scriptType = scriptType;
    }

    public CreateScriptPacket(String scriptName, String scriptPath) {
        this.scriptName = scriptName;
        this.scriptPath = scriptPath;
        this.scriptType = ScriptType.CLIENT;
    }

    public CreateScriptPacket() {

    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.scriptName);
        buf.writeString(this.scriptPath);
        buf.writeEnumConstant(this.scriptType);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scriptName = buf.readString();
        this.scriptPath = buf.readString();
        this.scriptType = buf.readEnumConstant(ScriptType.class);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "create_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<CreateScriptPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, CreateScriptPacket packet) {
            String code = packet.scriptType == ScriptType.SERVER ? ScriptExecutor.DEFAULT_SCRIPT : ScriptExecutor.DEFAULT_CLIENT_SCRIPT;

            CubeCode.projectManager.createTxtFile(packet.scriptName, packet.scriptPath, code);
            CubeCode.settingManager.addScript(packet.scriptName, packet.scriptType);

            if (packet.scriptType == ScriptType.CLIENT) {
                Dispatcher.sendToAll(new CreateScriptPacket(packet.scriptName, packet.scriptPath), server);
            }
        }
    }

    public static class ClientHandler implements ClientPacketHandler<CreateScriptPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, CreateScriptPacket packet) {
            CubeCodeClient.projectManager.createScript(new Script(packet.scriptPath, ScriptExecutor.DEFAULT_CLIENT_SCRIPT, ScriptType.CLIENT));
        }
    }
}

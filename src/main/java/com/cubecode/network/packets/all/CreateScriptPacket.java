package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.api.scripts.ServerScript;
import com.cubecode.client.scripts.ClientProjectManager;
import com.cubecode.client.scripts.ClientScript;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.ScriptSide;
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
    ScriptSide scriptSide;

    public CreateScriptPacket(String scriptName, String scriptPath, ScriptSide scriptSide) {
        this.scriptName = scriptName;
        this.scriptPath = scriptPath;
        this.scriptSide = scriptSide;
    }

    public CreateScriptPacket(String scriptName, String scriptPath) {
        this.scriptName = scriptName;
        this.scriptPath = scriptPath;
        this.scriptSide = ScriptSide.CLIENT;
    }

    public CreateScriptPacket() {

    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.scriptName);
        buf.writeString(this.scriptPath);
        buf.writeEnumConstant(this.scriptSide);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scriptName = buf.readString();
        this.scriptPath = buf.readString();
        this.scriptSide = buf.readEnumConstant(ScriptSide.class);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "create_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<CreateScriptPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, CreateScriptPacket packet) {
            String code = packet.scriptSide == ScriptSide.SERVER ? ProjectManager.DEFAULT_SCRIPT : ProjectManager.DEFAULT_CLIENT_SCRIPT;

            CubeCode.projectManager.createTxtFile(packet.scriptName, packet.scriptPath, code);
            CubeCode.settingManager.addScript(packet.scriptName, packet.scriptSide);

            if (packet.scriptSide == ScriptSide.CLIENT) {
                Dispatcher.sendToAll(new CreateScriptPacket(packet.scriptName, packet.scriptPath), server);
            }
        }
    }

    public static class ClientHandler implements ClientPacketHandler<CreateScriptPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, CreateScriptPacket packet) {
            CubeCodeClient.projectManager.createScript(new ClientScript(packet.scriptPath, ClientProjectManager.DEFAULT_SCRIPT));
        }
    }
}

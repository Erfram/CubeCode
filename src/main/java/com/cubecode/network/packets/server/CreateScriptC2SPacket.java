package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CreateScriptC2SPacket extends AbstractPacket {
    String scriptName;
    String scriptPath;

    public CreateScriptC2SPacket(String scriptName, String scriptPath) {
        this.scriptName = scriptName;
        this.scriptPath = scriptPath;
    }

    public CreateScriptC2SPacket() {

    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.scriptName);
        buf.writeString(this.scriptPath);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scriptName = buf.readString();
        this.scriptPath = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "create_script_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<CreateScriptC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, CreateScriptC2SPacket packet) {
            String code = ProjectManager.DEFAULT_SCRIPT;

            CubeCode.projectManager.createScript(packet.scriptName, packet.scriptPath, code);
            CubeCode.projectManager.updateScriptsFromFiles();
        }
    }
}

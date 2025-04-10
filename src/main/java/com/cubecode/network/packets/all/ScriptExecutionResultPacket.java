package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import com.cubecode.client.scripts.code.ClientScriptEvent;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.scripting.ScriptExecutionResult;
import com.cubecode.utils.CubeCodeException;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScriptExecutionResultPacket extends AbstractPacket {
    ScriptExecutionResult result;

    public ScriptExecutionResultPacket() {

    }

    public ScriptExecutionResultPacket(ScriptExecutionResult result) {
        this.result = result;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeBoolean(result.error);
        buf.writeInt(result.errorLine);
        buf.writeString(result.errorMessage);
        buf.writeString(result.result);
        buf.writeString(result.source);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        boolean error = buf.readBoolean();
        int errorLine = buf.readInt();
        String errorMessage = buf.readString();
        String result = buf.readString();
        String source = buf.readString();
        ScriptExecutionResult scriptExecutionResult = new ScriptExecutionResult(result);
        scriptExecutionResult.error = error;
        scriptExecutionResult.errorLine = errorLine;
        scriptExecutionResult.errorMessage = errorMessage;
        scriptExecutionResult.setSource(source);
        this.result = scriptExecutionResult;
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("script_execution_result");
    }

    public static class ServerHandler implements ServerPacketHandler<ScriptExecutionResultPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, ScriptExecutionResultPacket packet) {
            CubeCode.scriptManager.handleScriptExecutionResult(packet.result);
        }
    }
}

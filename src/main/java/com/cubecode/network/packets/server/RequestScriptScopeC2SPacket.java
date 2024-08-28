package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.ScriptScope;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.network.packets.client.UpdateScriptsS2CPacket;
import dev.latvian.mods.rhino.Scriptable;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class RequestScriptScopeC2SPacket extends AbstractPacket {
    public String scriptName;

    public RequestScriptScopeC2SPacket() {

    }

    public RequestScriptScopeC2SPacket(String scriptName) {
        this.scriptName = scriptName;
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
        return new Identifier(CubeCode.MOD_ID, "request_script_scope_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RequestScriptScopeC2SPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RequestScriptScopeC2SPacket packet) {
            Script script = CubeCode.scriptManager.getScript(packet.scriptName);
            script.evaluate();
            ArrayList<Scriptable> scopes = new ArrayList<>();
            scopes.add(script.scope);
            Scriptable parentScope = script.scope.getParentScope();
            while (parentScope != null) {
                scopes.add(parentScope);
                parentScope = parentScope.getParentScope();
            }
            System.out.println(packet.scriptName);
        }
    }
}

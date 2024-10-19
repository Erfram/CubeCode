package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.scripts.ProjectManager;
import com.cubecode.api.scripts.Script;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.network.packets.client.FillScriptScopeS2CPacket;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.PacketByteBufUtils;
import dev.latvian.mods.rhino.Scriptable;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;

public class RequestScriptScopeC2SPacket extends AbstractPacket {
    public Script script;

    public RequestScriptScopeC2SPacket() {

    }

    public RequestScriptScopeC2SPacket(Script script) {
        this.script = script;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        PacketByteBufUtils.writeScript(buf, this.script);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.script = PacketByteBufUtils.readScript(buf);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "request_script_scope_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<RequestScriptScopeC2SPacket> {

        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RequestScriptScopeC2SPacket packet) {
            Script script = packet.script;
            try {
                script.evaluate();
            } catch (CubeCodeException ignored) {
                player.closeHandledScreen();
            }
            ArrayList<Scriptable> scopes = new ArrayList<>();
            scopes.add(script.scope);
            Scriptable parentScope = script.scope.getParentScope();
            while (parentScope != null) {
                scopes.add(parentScope);
                parentScope = parentScope.getParentScope();
            }
            NbtCompound structure = new NbtCompound();
            NbtCompound prev = structure;
            for (int i = scopes.size() - 1; i >= 0; i--) {
                NbtCompound scope = new NbtCompound();
                NbtList keys = new NbtList();
                Arrays.stream(scopes.get(i).getAllIds(ProjectManager.globalContext)).forEach(id -> keys.add(NbtString.of((String) id)));
                scope.put("keys", keys);
                prev.put(scopes.get(i).toString(), scope);
                prev = scope;
            }

            Dispatcher.sendTo(new FillScriptScopeS2CPacket(structure), player);
        }
    }
}

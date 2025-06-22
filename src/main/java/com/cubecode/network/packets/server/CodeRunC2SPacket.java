package com.cubecode.network.packets.server;

import com.cubecode.CubeCode;
import com.cubecode.api.project.scripts.Properties;
import com.cubecode.api.project.scripts.ScriptExecutor;
import com.cubecode.api.project.scripts.ScriptScope;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ServerPacketHandler;
import dev.latvian.mods.rhino.Context;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CodeRunC2SPacket extends AbstractPacket {
    String code;

    public CodeRunC2SPacket() {}

    public CodeRunC2SPacket(String code) {
        this.code = code;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(this.code);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.code = buf.readString();
    }

    @Override
    public Identifier getIdentifier() {
        return CubeCode.createId("code_run_c2s");
    }

    public static class ServerHandler implements ServerPacketHandler<CodeRunC2SPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, CodeRunC2SPacket packet) {
            Properties properties = Properties.create(
                    "test",
                    "server",
                    player,
                    null,
                    player.getWorld(),
                    server
            );

            Context context = Context.enter();
            ScriptScope scope = new ScriptScope("test", context);
            scope.setParentScope(ScriptExecutor.globalScope);
            CubeCode.projectManager.executor.evaluate(context, scope, packet.code, "test");
            CubeCode.projectManager.executor.invokeFunction(context, scope, "server", properties.getMap().values().toArray());
        }
    }
}

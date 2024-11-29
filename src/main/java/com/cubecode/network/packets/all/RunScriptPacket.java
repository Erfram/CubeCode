package com.cubecode.network.packets.all;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.ServerScript;
import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import com.cubecode.client.scripts.ClientProperties;
import com.cubecode.client.scripts.ClientScript;
import com.cubecode.client.scripts.code.ClientScriptEvent;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.CubeCodeException;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RunScriptPacket extends AbstractPacket {
    String scriptName;
    String function = "main";
    NbtCompound nbt;
    public RunScriptPacket() {

    }

    public RunScriptPacket(String scriptName, NbtCompound nbt) {
        this.scriptName = scriptName;
        this.nbt = nbt;
    }

    public RunScriptPacket(String scriptName, String function, NbtCompound nbt) {
        this.scriptName = scriptName;
        this.function = function;
        this.nbt = nbt;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeString(scriptName);
        buf.writeNbt(nbt);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        scriptName = buf.readString();
        nbt = buf.readNbt();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("run_script");
    }

    public static class ClientHandler implements ClientPacketHandler<RunScriptPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, RunScriptPacket packet) {
            ClientScript script = CubeCodeClient.projectManager.getScript(packet.scriptName);

            if (script != null) {
                ClientProperties properties = ClientProperties.create(script.getName(), packet.function, client.player, null, client.world);

                ClientScriptEvent scriptEvent = (ClientScriptEvent) properties.get("Context");

                scriptEvent.setValue("data", new ScriptNbtCompound(packet.nbt));

                try {
                    script.run(properties);
                } catch(CubeCodeException e){
                    client.player.sendMessage(Text.of("§c" + e.getMessage()));
                }
            }
        }
    }

    public static class ServerHandler implements ServerPacketHandler<RunScriptPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, RunScriptPacket packet) {
            ServerScript script = CubeCode.projectManager.getScript(packet.scriptName);

            if (script != null) {
                Properties properties = Properties.create(script.getName(), packet.function, player, null, player.getWorld(), server);

                ScriptEvent scriptEvent = (ScriptEvent) properties.get("Context");

                scriptEvent.setValue("data", new ScriptNbtCompound(packet.nbt));

                try {
                    script.run(packet.scriptName, properties);
                } catch (CubeCodeException cce) {
                    player.sendMessage(Text.of("§c" + cce.getMessage()));
                    cce.printStackTrace();
                }
            }
        }
    }
}

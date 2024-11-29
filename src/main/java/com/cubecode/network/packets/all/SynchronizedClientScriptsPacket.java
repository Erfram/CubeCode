package com.cubecode.network.packets.all;

import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.ServerScript;
import com.cubecode.client.scripts.ClientScript;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.network.basic.ServerPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import com.cubecode.utils.Script;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedClientScriptsPacket extends AbstractPacket {
    List<ServerScript> scripts;

    public SynchronizedClientScriptsPacket() {

    }

    public SynchronizedClientScriptsPacket(List<ServerScript> scripts) {
        this.scripts = scripts;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeCollection(this.scripts, PacketByteBufUtils::writeScript);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.scripts = buf.readCollection(ArrayList::new, PacketByteBufUtils::readScript);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("sync_client_scripts");
    }

    public static class ClientHandler implements ClientPacketHandler<SynchronizedClientScriptsPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, SynchronizedClientScriptsPacket packet) {
            List<ClientScript> clientScripts = new ArrayList<>();

            for (Script script : packet.scripts) {
                ClientScript clientScript = new ClientScript(script.getName(), script.getCode());
                clientScript.setLibraries(script.getLibraries());

                clientScripts.add(clientScript);
            }

            CubeCodeClient.projectManager.setScripts(clientScripts);
        }
    }

    public static class ServerHandler implements ServerPacketHandler<SynchronizedClientScriptsPacket> {
        @Override
        public void run(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketSender responseSender, SynchronizedClientScriptsPacket packet) {
            Dispatcher.sendToAll(new SynchronizedClientScriptsPacket(packet.scripts), server);
        }
    }
}
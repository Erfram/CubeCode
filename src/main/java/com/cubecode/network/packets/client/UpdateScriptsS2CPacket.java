package com.cubecode.network.packets.client;

import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.views.textEditor.TextEditorView;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.utils.PacketByteBufUtils;
import com.google.common.collect.ConcurrentHashMultiset;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UpdateScriptsS2CPacket extends AbstractPacket {
    List<Script> scripts = new ArrayList<>();

    public UpdateScriptsS2CPacket(List<Script> scripts) {
        this.scripts = scripts;
    }

    public UpdateScriptsS2CPacket() {

    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        buf.writeCollection(
                this.scripts,
                PacketByteBufUtils::writeScript
        );
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        this.scripts = buf.readList(PacketByteBufUtils::readScript);
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("update_scripts_s2c");
    }

    public static class ClientHandler implements ClientPacketHandler<UpdateScriptsS2CPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, UpdateScriptsS2CPacket packet) {
            CubeCodeClient.scripts = new CopyOnWriteArrayList<>(packet.scripts);
            TextEditorView.scripts = new CopyOnWriteArrayList<>(packet.scripts);
        }
    }
}

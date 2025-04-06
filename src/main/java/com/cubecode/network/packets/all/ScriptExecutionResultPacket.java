package com.cubecode.network.packets.all;

import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import com.cubecode.client.scripts.code.ClientScriptEvent;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import com.cubecode.utils.CubeCodeException;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ScriptExecutionResultPacket extends AbstractPacket {
    @Override
    public void toBytes(PacketByteBuf buf) {

    }

    @Override
    public void fromBytes(PacketByteBuf buf) {

    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("script_notify");
    }

    public static class ClientHandler implements ClientPacketHandler<ScriptExecutionResultPacket> {
        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, ScriptExecutionResultPacket packet) {

        }
    }
}

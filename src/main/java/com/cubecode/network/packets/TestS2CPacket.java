package com.cubecode.network.packets;

import com.cubecode.network.basic.AbstractPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TestS2CPacket extends AbstractPacket {
    String str;

    public TestS2CPacket(String str) {
        this.str = str;
    }

    public TestS2CPacket() {

    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier("test_s2c_packet");
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        buf.writeString(this.str);
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        this.str = buf.readString();
    }

    @Override
    public void client(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        MinecraftClient.getInstance().player.sendMessage(Text.of(this.str));
    }
}

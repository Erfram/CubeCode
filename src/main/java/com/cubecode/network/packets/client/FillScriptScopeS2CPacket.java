package com.cubecode.network.packets.client;

import com.cubecode.CubeCode;
import com.cubecode.client.views.textEditor.ScopeView;
import com.cubecode.network.basic.AbstractPacket;
import com.cubecode.network.basic.ClientPacketHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class FillScriptScopeS2CPacket extends AbstractPacket {
    public NbtCompound structure;

    public FillScriptScopeS2CPacket() {

    }

    public FillScriptScopeS2CPacket(NbtCompound structure) {
        this.structure = structure;
    }

    @Override
    public void toBytes(PacketByteBuf buf) {
        buf.writeNbt(this.structure);
    }

    @Override
    public void fromBytes(PacketByteBuf buf) {
        this.structure = buf.readNbt();
    }

    @Override
    public Identifier getIdentifier() {
        return new Identifier(CubeCode.MOD_ID, "fill_script_scope_s2c");
    }

    public static class ClientHandler implements ClientPacketHandler<FillScriptScopeS2CPacket> {

        @Override
        public void run(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender, FillScriptScopeS2CPacket packet) {
            ScopeView.setStructure(packet.structure);
        }
    }
}


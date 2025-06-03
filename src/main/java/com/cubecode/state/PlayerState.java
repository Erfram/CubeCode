package com.cubecode.state;

import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.PlayerStateSyncPacket;
import com.cubecode.utils.Serializable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerState implements Serializable {
    PlayerEntity player;

    NbtCompound states = new NbtCompound();

    public PlayerState(PlayerEntity player) {
        this.player = player;
    }

    public PlayerState() {

    }

    public void setStates(NbtCompound states) {
        this.states = states;
    }

    public NbtCompound getStates() {
        return this.states;
    }

    public void syncToClient() {
        if (this.player != null)
            Dispatcher.sendTo(new PlayerStateSyncPacket(this), (ServerPlayerEntity) player);
    }

    public void syncToServer() {
        if (player.getWorld().isClient)
            Dispatcher.sendToServer(new PlayerStateSyncPacket(this));
    }

    @Override
    public NbtCompound serialize() {
        NbtCompound nbt = new NbtCompound();

        nbt.put("states", this.states);

        return nbt;
    }

    @Override
    public void deserialize(NbtCompound nbt) {
        this.states = nbt.getCompound("states");
    }
}

package com.cubecode.api.scripts.code.cubecode;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import com.cubecode.state.ServerState;

import java.util.List;

public class CubeCodeStates {
    NbtCompound values;

    public CubeCodeStates(MinecraftServer server) {
        this.values = ServerState.getServerState(server).values;
    }

    public CubeCodeStates(PlayerEntity player) {
        this.values = ServerState.getPlayerState(player).getValues();
    }

    public List<String> getKeys() {
        return this.values.getKeys().stream().toList();
    }

    public void putString(String key, String value) {
        this.values.putString(key, value);
    }

    public void putInt(String key, int value) {
        this.values.putInt(key, value);
    }

    public void putDouble(String key, double value) {
        this.values.putDouble(key, value);
    }

    public void putFloat(String key, float value) {
        this.values.putFloat(key, value);
    }

    public void putBoolean(String key, boolean value) {
        this.values.putBoolean(key, value);
    }

    public void putByte(String key, byte value) {
        this.values.putByte(key, value);
    }

    public void putNbt(String key, NbtCompound value) {
        this.values.put(key, value);
    }

    public String getString(String key) {
        return this.values.getString(key);
    }

    public int getInt(String key) {
        return this.values.getInt(key);
    }

    public double getDouble(String key) {
        return this.values.getDouble(key);
    }

    public float getFloat(String key) {
        return this.values.getFloat(key);
    }

    public boolean getBoolean(String key) {
        return this.values.getBoolean(key);
    }

    public byte getByte(String key) {
        return this.values.getByte(key);
    }

    public NbtCompound getNbt(String key) {
        return this.values.getCompound(key);
    }
 }

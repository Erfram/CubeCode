package com.cubecode.api.scripts.code.cubecode;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import com.cubecode.state.ServerState;

import java.util.List;

/**
 * Docs for states bruh
 *
 * <pre>{@code
 * function main(c) {
 *     let states = c.getPlayer().getStates();
 *     let array = [1337, "TheBendy"];
 *
 *     states.putString("example", JSON.stringify(array));  // "[1337,\"TheBendy\"]"
 *     c.player.send(array == JSON.parse(states.getString("example"))); // true
 * }
 * }</pre>
 */
public class CubeCodeStates {
    NbtCompound values;
    MinecraftServer server;
    PlayerEntity player;

    public CubeCodeStates(MinecraftServer server) {
        this.values = ServerState.getServerState(server).values;
        this.server = server;
    }

    public CubeCodeStates(PlayerEntity player) {
        this.values = ServerState.getPlayerState(player).getValues();
        this.player = player;
    }

    /**
     * Returns a list of all state keys
     * <pre>{@code
     * let keys = c.getPlayer().getStates().getKeys();
     *
     * c.getPlayer().send(keys["example"]); //1337
     * }</pre>
     */
    public List<String> getKeys() {
        return this.values.getKeys().stream().toList();
    }

    /**
     * <pre>{@code
     * c.getPlayer().getStates().putString("example", "Name");
     * }</pre>
     */
    public void putString(String key, String value) {
        this.values.putString(key, value);

        this.saveStates();
    }

    /**
     * Adds an integer value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().putInt("example", 2204);
     * }</pre>
     */
    public void putInt(String key, int value) {
        this.values.putInt(key, value);

        this.saveStates();
    }

    /**
     * Adds a double value for the specified key
     * <pre>{@code
     * c.getPlayer().getStates().putDouble("example", 100.8974);
     * }</pre>
     */
    public void putDouble(String key, double value) {
        this.values.putDouble(key, value);

        this.saveStates();
    }

    /**
     * Adds a float value for the specified key
     * <pre>{@code
     * c.getPlayer().getStates().putFloat("example", 100.89);
     * }</pre>
     */
    public void putFloat(String key, float value) {
        this.values.putFloat(key, value);

        this.saveStates();
    }

    /**
     * Adds a boolean value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().putBoolean("example", true);
     * }</pre>
     */
    public void putBoolean(String key, boolean value) {
        this.values.putBoolean(key, value);

        this.saveStates();
    }

    /**
     * Adds a byte value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().putByte("example", 127);
     * }</pre>
     */
    public void putByte(String key, byte value) {
        this.values.putByte(key, value);

        this.saveStates();
    }

    /**
     * Adds an NbtCompound object for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().putNbt("example", CubeCode.createCompound("{}"));
     * }</pre>
     */
    public void putNbt(String key, NbtCompound value) {
        this.values.put(key, value);

        this.saveStates();
    }

    /**
     * Returns the string value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getString("example");
     * }</pre>
     */
    public String getString(String key) {
        return this.values.getString(key);
    }

    /**
     * Returns the integer value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getInt("example");
     * }</pre>
     */
    public int getInt(String key) {
        return this.values.getInt(key);
    }

    /**
     * Returns the double value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getDouble("example");
     * }</pre>
     */
    public double getDouble(String key) {
        return this.values.getDouble(key);
    }

    /**
     * Returns the float value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getFloat("example");
     * }</pre>
     */
    public float getFloat(String key) {
        return this.values.getFloat(key);
    }

    /**
     * Returns the boolean value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getBoolean("example");
     * }</pre>
     */
    public boolean getBoolean(String key) {
        return this.values.getBoolean(key);
    }

    /**
     * Returns the byte value for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getByte("example");
     * }</pre>
     */
    public byte getByte(String key) {
        return this.values.getByte(key);
    }

    /**
     * Returns the NbtCompound object for the specified key
     *
     * <pre>{@code
     * c.getPlayer().getStates().getNbt("example");
     * }</pre>
     */
    public NbtCompound getNbt(String key) {
        return this.values.getCompound(key);
    }

    private void saveStates() {
        if (this.server != null) {
            ServerState.getServerState(this.server).values = this.values;
            ServerState.getServerState(this.server).markDirty();
        } else {
            ServerState.getPlayerState(this.player).setValues(this.values);
            ServerState.getServerState(this.player.getServer()).markDirty();
        }
    }
}

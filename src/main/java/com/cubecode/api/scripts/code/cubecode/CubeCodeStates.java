package com.cubecode.api.scripts.code.cubecode;

import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import com.cubecode.state.ServerState;

import java.util.List;

/**
 * CubeCodeStates
 *
 * Overview:
 * CubeCodeStates provides a persistent key-value storage system that can be accessed from both player and server contexts. It supports various data types and can be used to store game state information.
 *
 * Getting States Object:
 * States can be accessed from both player and server contexts using the getStates() method.
 *
 * <pre>{@code
 * function main(c) {
 *     const states = c.getPlayer().getStates();
 *     const array = [1337, "TheBendy"];
 *
 *     states.putString("example", JSON.stringify(array));  // "[1337,\"TheBendy\"]"
 *     c.player.send(array == JSON.parse(states.getString("example")), false); // true
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
     * Returns a list of all stored keys.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const keys = states.getKeys();
     * keys.forEach(key => {
     *     c.log(`Found key: ${key}`);
     * });
     * }</pre>
     */
    public List<String> getKeys() {
        return this.values.getKeys().stream().toList();
    }

    /**
     * Stores a string value associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * states.putString("cool_player", "theuran");
     * }</pre>
     */
    public void putString(String key, String value) {
        this.values.putString(key, value);

        this.saveStates();
    }

    /**
     * Stores an integer value associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * states.putInt("playerCount", 5);
     * }</pre>
     */
    public void putInt(String key, int value) {
        this.values.putInt(key, value);

        this.saveStates();
    }

    /**
     * Stores a double value associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * states.putDouble("exactPosition", 123.456);
     * }</pre>
     */
    public void putDouble(String key, double value) {
        this.values.putDouble(key, value);

        this.saveStates();
    }

    /**
     * Stores a float value associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * states.putFloat("speed", 1.5);
     * }</pre>
     */
    public void putFloat(String key, float value) {
        this.values.putFloat(key, value);

        this.saveStates();
    }

    /**
     * Stores a boolean value associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * states.putBoolean("isEventActive", true);
     * }</pre>
     */
    public void putBoolean(String key, boolean value) {
        this.values.putBoolean(key, value);

        this.saveStates();
    }

    /**
     * Stores a byte value associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * states.putByte("flag", 1);
     * }</pre>
     */
    public void putByte(String key, byte value) {
        this.values.putByte(key, value);

        this.saveStates();
    }

    /**
     * Stores an NBT compound associated with the specified key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const nbt = CubeCode.createCompound(`{"name":"test"}`);
     * states.putNbt("customData", nbt);
     * }</pre>
     */
    public void putNbt(String key, ScriptNbtCompound value) {
        this.values.put(key, value.getMinecraftNbtCompound().copy());

        this.saveStates();
    }

    /**
     * Retrieves a string value by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const lastPlayer = states.getString("lastPlayer");
     * c.log(`Last player was: ${lastPlayer}`);
     * }</pre>
     */
    public String getString(String key) {
        return this.values.getString(key);
    }

    /**
     * Retrieves an integer value by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const count = states.getInt("playerCount");
     * c.server.send(`Player count: ${count}`, false);
     * }</pre>
     */
    public int getInt(String key) {
        return this.values.getInt(key);
    }

    /**
     * Retrieves a double value by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const position = states.getDouble("exactPosition");
     * c.server.send(`Position: ${position}`, false);
     * }</pre>
     */
    public double getDouble(String key) {
        return this.values.getDouble(key);
    }

    /**
     * Retrieves a float value by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const speed = states.getFloat("speed");
     * c.server.send(`Speed: ${speed}`, false);
     * }</pre>
     */
    public float getFloat(String key) {
        return this.values.getFloat(key);
    }

    /**
     * Retrieves a boolean value by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const isActive = states.getBoolean("isEventActive");
     * c.server.send(`Event active: ${isActive}`, false);
     * }</pre>
     */
    public boolean getBoolean(String key) {
        return this.values.getBoolean(key);
    }

    /**
     * Retrieves a byte value by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const flag = states.getByte("flag");
     * c.server.send(`Flag value: ${flag}`, false);
     * }</pre>
     */
    public byte getByte(String key) {
        return this.values.getByte(key);
    }

    /**
     * Retrieves an NBT compound by its key.
     *
     * <pre>{@code
     * const states = c.server.getStates();
     * const data = states.getNbt("customData");
     * const name = data.getString("name");
     * c.server.send(`Custom data name: ${name}`, false);
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

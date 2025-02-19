package com.cubecode.api.scripts.code.entities;

import com.cubecode.api.scripts.code.items.ScriptInventory;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.RunScriptPacket;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.world.GameMode;
import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.cubecode.CubeCodeStates;

/**
 * Tab with methods that can be used on players.
 *
 * <pre>{@code
 * function server(c) {
 *     let player = c.getPlayer();
 *     let states = player.getStates();
 *
 *     states.putString("example", "Minecraft");
 *     player.send(states.getString("example"));
 * }
 * }</pre>
 */
public class ScriptPlayer extends ScriptEntity<ServerPlayerEntity> {
    public ScriptPlayer(ServerPlayerEntity entity) {
        super(entity);
    }

    /**
     * Returns the Minecraft player entity associated with this script player
     */
    public ServerPlayerEntity getMinecraftPlayer() {
        return this.entity;
    }

    /**
     * Returns the CubeCode states for this player
     */
    public CubeCodeStates getStates() {
        return new CubeCodeStates(this.entity);
    }

    /**
     * Sends a message to the player
     *
     * <pre>{@code
     * c.getPlayer().send("Kukold");
     * }</pre>
     */
    public void send(String message) {
        this.entity.sendMessage(Text.of(message));
    }

    /**
     * Sets the player's rotation angles
     * <pre>{@code
     * c.getPlayer().setRotations(90, 90, 0);
     * }</pre>
     */
    @Override
    public void setRotations(float pitch, float yaw, float headYaw) {
        this.entity.networkHandler.requestTeleport(
                this.getPosition().x,
                this.getPosition().y,
                this.getPosition().z,
                pitch,
                yaw
        );
    }

    /**
     * Sets the player's velocity
     *
     * <pre>{@code
     * c.getPlayer().setVelocity(0, 1, 0);
     * }</pre>
     */
    @Override
    public void setVelocity(double x, double y, double z) {
        super.setVelocity(x, y, z);
        this.entity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(this.entity));
    }

    /**
     * Adds to the player's current velocity
     *
     * <pre>{@code
     * c.getPlayer().addVelocity(0, 1, 0);
     * }</pre>
     */
    @Override
    public void addVelocity(double x, double y, double z) {
        super.addVelocity(x, y, z);
        this.entity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(this.entity));
    }

    /**
     * Executes a command as the player
     *
     * <pre>{@code
     * c.getPlayer().executeCommand("say @s never using this method!");
     * }</pre>
     */
    public void executeCommand(String command) {
        this.entity.getServer().getCommandManager().executeWithPrefix(this.entity.getCommandSource(), command);
    }

    /**
     * Returns the player's current game mode
     */
    public String getGameMode() {
        return this.entity.interactionManager.getGameMode().getName();
    }

    /**
     * Sets the player's game mode
     *
     * <pre>{@code
     * c.getPlayer().setGameMode("creative");
     * }</pre>
     */
    public void setGameMode(String gameMode) {
        this.entity.changeGameMode(GameMode.byName(gameMode));
    }

    /**
     * Sets the player's spawn point to their current location
     */
    public void setSpawnPoint() {
        this.entity.setSpawnPoint(this.entity.getWorld().getRegistryKey(), this.entity.getBlockPos(), 0, true, false);
    }

    /**
     * Sets the player's spawn point in the specified world, coordinates, and angle
     *
     * <pre>{@code
     * c.getPlayer().setSpawnPoint(0, 0, 0, 90);
     * }</pre>
     */
    public void setSpawnPoint(int x, int y, int z, float angle) {
        this.entity.setSpawnPoint(this.entity.getWorld().getRegistryKey(), new BlockPos(x, y, z), angle, true, false);
    }

    /**
     * Sets the player's spawn point in the specified world, coordinates, and angle
     *
     * <pre>{@code
     * c.getPlayer().setSpawnPoint(c.getWorld(), 2232, 23, 232, 90);
     * }</pre>
     */
    public void setSpawnPoint(ScriptWorld world, int x, int y, int z, float angle) {
        this.entity.setSpawnPoint(world.getMinecraftWorld().getRegistryKey(), new BlockPos(x, y, z), angle, true, false);
    }

    /**
     * Returns the player's current spawn point
     */
    public ScriptVector getSpawnPoint() {
        BlockPos pos = this.entity.getSpawnPointPosition();
        return new ScriptVector(pos == null ? this.entity.getWorld().getSpawnPos() : pos);
    }

    /**
     * Checks if the player is currently flying
     */
    public boolean isFlying() {
        return this.entity.getAbilities().flying;
    }

    /**
     * Sets the player's flying speed
     */
    public void setFlySpeed(float flySpeed) {
        this.entity.getAbilities().setFlySpeed(flySpeed);
    }

    /**
     * Returns the player's current walking speed
     */
    public float getWalkSpeed() {
        return this.entity.getAbilities().getWalkSpeed();
    }

    /**
     * Sets the player's walking speed
     */
    public void setWalkSpeed(float walkSpeed) {
        this.entity.getAbilities().setWalkSpeed(walkSpeed);
    }

    /**
     * Resets the player's walking speed to the default value
     */
    public void resetWalkSpeed() {
        this.setWalkSpeed(0.1F);
    }

    /**
     * Plays a static sound for the player at the specified location
     *
     * <pre>{@code
     * c.getPlayer().playStaticSound("block.anvil.break", "master", 784, 89, 884, 1, 1);
     * }</pre>
     */
    public void playStaticSound(String soundEvent, String soundCategory, double x, double y, double z, float volume, float pitch) {
        this.entity.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvent.of(new Identifier(soundEvent))), SoundCategory.valueOf(soundCategory), x, y, z, volume, pitch, RandomSeed.getSeed()));
    }

    /**
     * Stops a static sound for the player
     *
     * <pre>{@code
     * c.getPlayer().stopStaticSound();
     * }</pre>
     */
    public void stopStaticSound(String soundId, String soundCategory) {
        this.entity.networkHandler.sendPacket(new StopSoundS2CPacket(new Identifier(soundId), SoundCategory.valueOf(soundCategory)));
    }

    /**
     * Returns player inventory
     */
    public ScriptInventory getInventory() {
        return new ScriptInventory(this.entity.getInventory());
    }

    /**
     * Runs a client script.
     *
     * Example:
     * <pre>{@code
     * //Server Script: a.js | Client Script: b.js
     *
     * // a.js
     * function server(c) {
     * 	c.getPlayer().sendTo("b.js")
     * }
     *
     * // b.js
     * function client(c) {
     * 	c.player.send("Hello Client!")
     * }
     * }</pre>
     */
    public void sendTo(String scriptName) {
        Dispatcher.sendTo(new RunScriptPacket(scriptName, new NbtCompound()), this.entity);
    }

    /**
     * Runs a client script. You can pass nbt to use it in some way on the client side
     *
     * Example:
     * <pre>{@code
     * //Server Script: a.js | Client Script: b.js
     *
     * // a.js
     * function server(c) {
     * 	c.getPlayer().sendTo(
     * 		"b.js",
     * 		CubeCode.createCompound("{\"a\":\"b\"}")
     * 	)
     * }
     *
     * // b.js
     * function client(c) {
     *  var data = c.getValue("data") //ScriptNbtCompound
     * 	c.player.send("Hello Client!")
     * 	c.player.send("Data: " + data.getString("a"))
     * }
     *
     * }</pre>
     */
    public void sendTo(String scriptName, ScriptNbtCompound nbt) {
        Dispatcher.sendTo(new RunScriptPacket(scriptName, nbt.getMinecraftNbtCompound()), this.entity);
    }

    /**
     * Runs a client script and other function.
     *
     * Example:
     * <pre>{@code
     * //Server Script: a.js | Client Script: b.js
     *
     * // a.js
     * function server(c) {
     * 	c.getPlayer().sendTo(
     * 		"b.js",
     * 		"fff"
     * 	)
     * }
     *
     * // b.js
     * function fff(c) {
     * 	c.player.send("Hello Client!")
     * }
     *
     * }</pre>
     */
    public void sendTo(String script, String function) {
        Dispatcher.sendTo(new RunScriptPacket(script, function, new NbtCompound()), this.entity);
    }

    /**
     * Runs a client script. You can pass nbt to use it in some way on the client side and other function
     *
     * Example:
     * <pre>{@code
     * //Server Script: a.js | Client Script: b.js
     *
     * // a.js
     * function server(c) {
     * 	c.getPlayer().sendTo(
     * 		"b.js",
     * 		CubeCode.createCompound("{\"a\":\"b\"}")
     * 	)
     * }
     *
     * // b.js
     * function client(c) {
     *  var data = c.getValue("data") //ScriptNbtCompound
     * 	c.player.send("Hello Client!")
     * 	c.player.send("Data: " + data.getString("a"))
     * }
     *
     * }</pre>
     */
    public void sendTo(String script, String function, ScriptNbtCompound nbt) {
        Dispatcher.sendTo(new RunScriptPacket(script, function, nbt.getMinecraftNbtCompound()), this.entity);
    }
}
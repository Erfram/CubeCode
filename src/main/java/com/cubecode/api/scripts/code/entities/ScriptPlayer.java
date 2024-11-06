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

public class ScriptPlayer extends ScriptEntity<ServerPlayerEntity> {
    public ScriptPlayer(ServerPlayerEntity entity) {
        super(entity);
    }

    public ServerPlayerEntity getMinecraftPlayer() {
        return this.entity;
    }

    public CubeCodeStates getStates() {
        return new CubeCodeStates(this.entity);
    }

    public void send(String message) {
        this.entity.sendMessage(Text.of(message));
    }

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

    @Override
    public void setVelocity(double x, double y, double z) {
        super.setVelocity(x, y, z);
        this.entity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(this.entity));
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        super.addVelocity(x, y, z);
        this.entity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(this.entity));
    }

    public void executeCommand(String command) {
        this.entity.getServer().getCommandManager().executeWithPrefix(this.entity.getCommandSource(), command);
    }

    public String getGameMode() {
        return this.entity.interactionManager.getGameMode().getName();
    }

    public void setGameMode(String gameMode) {
        this.entity.changeGameMode(GameMode.byName(gameMode));
    }

    public void setSpawnPoint() {
        this.entity.setSpawnPoint(this.entity.getWorld().getRegistryKey(), this.entity.getBlockPos(), 0, true, false);
    }

    public void setSpawnPoint(int x, int y, int z, float angle) {
        this.entity.setSpawnPoint(this.entity.getWorld().getRegistryKey(), new BlockPos(x, y, z), angle, true, false);
    }

    public void setSpawnPoint(ScriptWorld world, int x, int y, int z, float angle) {
        this.entity.setSpawnPoint(world.getMinecraftWorld().getRegistryKey(), new BlockPos(x, y, z), angle, true, false);
    }

    public ScriptVector getSpawnPoint() {
        BlockPos pos = this.entity.getSpawnPointPosition();
        return new ScriptVector(pos == null ? this.entity.getWorld().getSpawnPos() : pos);
    }

    public boolean isFlying() {
        return this.entity.getAbilities().flying;
    }

    public void setFlySpeed(float flySpeed) {
        this.entity.getAbilities().setFlySpeed(flySpeed);
    }

    public float getWalkSpeed() {
        return this.entity.getAbilities().getWalkSpeed();
    }

    public void setWalkSpeed(float walkSpeed) {
        this.entity.getAbilities().setWalkSpeed(walkSpeed);
    }

    public void resetWalkSpeed() {
        this.setWalkSpeed(0.1F);
    }

    public void playStaticSound(String soundEvent, String soundCategory, double x, double y, double z, float volume, float pitch) {
        this.entity.networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvent.of(new Identifier(soundEvent))), SoundCategory.valueOf(soundCategory), x, y, z, volume, pitch, RandomSeed.getSeed()));
    }

    public void stopStaticSound(String soundId, String soundCategory) {
        this.entity.networkHandler.sendPacket(new StopSoundS2CPacket(new Identifier(soundId), SoundCategory.valueOf(soundCategory)));
    }

    public ScriptInventory getInventory() {
        return new ScriptInventory(this.entity.getInventory());
    }

    public void sendTo(String script, ScriptNbtCompound nbt) {
        Dispatcher.sendTo(new RunScriptPacket(script, nbt.getMinecraftNbtCompound()), this.entity);
    }
}
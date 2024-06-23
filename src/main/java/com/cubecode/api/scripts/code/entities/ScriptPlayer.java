package com.cubecode.api.scripts.code.entities;

import net.minecraft.entity.player.PlayerEntity;
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

public class ScriptPlayer extends ScriptEntity<PlayerEntity> {
    public ScriptPlayer(PlayerEntity entity) {
        super(entity);
    }

    public PlayerEntity getMinecraftPlayer() {
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
        ((ServerPlayerEntity)this.entity).networkHandler.requestTeleport(
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
        ((ServerPlayerEntity)this.entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(this.entity));
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        super.addVelocity(x, y, z);
        ((ServerPlayerEntity)this.entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(this.entity));
    }

    public void executeCommand(String command) {
        this.entity.getServer().getCommandManager().executeWithPrefix(this.entity.getCommandSource(), command);
    }

    public String getGameMode() {
        return ((ServerPlayerEntity)this.entity).interactionManager.getGameMode().getName();
    }

    public void setGameMode(String gameMode) {
        ((ServerPlayerEntity)this.entity).changeGameMode(GameMode.byName(gameMode));
    }

    public void setSpawnPoint() {
        ((ServerPlayerEntity)this.entity).setSpawnPoint(this.entity.getWorld().getRegistryKey(), this.entity.getBlockPos(), 0, true, false);
    }

    public void setSpawnPoint(int x, int y, int z, float angle) {
        ((ServerPlayerEntity)this.entity).setSpawnPoint(this.entity.getWorld().getRegistryKey(), new BlockPos(x, y, z), angle, true, false);
    }

    public void setSpawnPoint(ScriptWorld world, int x, int y, int z, float angle) {
        ((ServerPlayerEntity)this.entity).setSpawnPoint(world.getMinecraftWorld().getRegistryKey(), new BlockPos(x, y, z), angle, true, false);
    }

    public ScriptVector getSpawnPoint() {
        BlockPos pos = ((ServerPlayerEntity)this.entity).getSpawnPointPosition();
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
        ((ServerPlayerEntity)this.entity).networkHandler.sendPacket(new PlaySoundS2CPacket(RegistryEntry.of(SoundEvent.of(new Identifier(soundEvent))), SoundCategory.valueOf(soundCategory), x, y, z, volume, pitch, RandomSeed.getSeed()));
    }

    public void stopStaticSound(String soundId, String soundCategory) {
        ((ServerPlayerEntity)this.entity).networkHandler.sendPacket(new StopSoundS2CPacket(new Identifier(soundId), SoundCategory.valueOf(soundCategory)));
    }
}
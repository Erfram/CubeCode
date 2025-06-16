package com.cubecode.api.project.scripts.code.entities;

import com.cubecode.api.project.scripts.code.ScriptVector;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

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


    public void setGameMode(int id) {
        this.entity.interactionManager.changeGameMode(GameMode.byId(id));
    }

    public void setGameMode(String name) {
        this.entity.interactionManager.changeGameMode(GameMode.byName(name));
    }

    public void getGameMode() {
        this.entity.interactionManager.getGameMode();
    }

    public void addExhaustion(float exhaustion) {
        this.entity.addExhaustion(exhaustion);
    }

    public void addExperience(int exp) {
        this.entity.addExperience(exp);
    }

    public void addExperienceLevels(int exp) {
        this.entity.addExperienceLevels(exp);
    }

    public void addScore(int score) {
        this.entity.addScore(score);
    }

    public void attack(Entity target) {
        this.entity.attack(target);
    }

    public void closeHandledScreen() {
        this.entity.closeHandledScreen();
    }

    public void disableShield(boolean disable) {
        this.entity.disableShield(disable);
    }

    public void jump() {
        this.entity.jump();
    }

    public void requestRespawn() {
        this.entity.requestRespawn();
    }

    public void resetLastAttackedTicks() {
        this.entity.resetLastAttackedTicks();
    }

    public void setAbsorptionAmount(float amount) {
        this.entity.setAbsorptionAmount(amount);
    }

    public void setCameraEntity(Entity entity) {
        this.entity.setCameraEntity(entity);
    }

    public void setExperienceLevel(int exp) {
        this.entity.setExperienceLevel(exp);
    }

    public void setExperiencePoints(int exp) {
        this.entity.setExperiencePoints(exp);
    }

    public void setReducedDebugInfo(boolean enabled) {
        this.entity.setReducedDebugInfo(enabled);
    }

    public void setWorld(String world) {
        this.entity.setServerWorld(this.entity.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier(world))));
    }

    public void setSpawnPoint(String dimension, ScriptVector vector) {
        this.entity.setSpawnPoint(RegistryKey.of(RegistryKeys.WORLD, new Identifier(dimension)), vector.toBlockPos(), 0f, false, false);
    }

    public void setSpawnPoint(String dimension, double x, double y, double z) {
        this.entity.setSpawnPoint(RegistryKey.of(RegistryKeys.WORLD, new Identifier(dimension)), new ScriptVector(x, y, z).toBlockPos(), 0f, false, false);
    }

    public void sleep(double x, double y, double z) {
        this.entity.sleep(new ScriptVector(x, y, z).toBlockPos());
    }

    public void sleep(ScriptVector vector) {
        this.entity.sleep(vector.toBlockPos());
    }

    public void startFallFlying() {
        this.entity.startFallFlying();
    }

    public void stopFallFlying() {
        this.entity.stopFallFlying();
    }

    public void tiltScreen(double deltaX, double deltaZ) {
        this.entity.tiltScreen(deltaX, deltaZ);
    }

    public void wakeUp() {
        this.entity.wakeUp();
    }

    public void wakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers) {
        this.entity.wakeUp(skipSleepTimer, updateSleepingPlayers);
    }





    public boolean checkFallFlying() {
        return this.entity.checkFallFlying();
    }

    public boolean hasReducedDebugInfo() {
        return this.entity.hasReducedDebugInfo();
    }

    public boolean isCreative() {
        return this.entity.isCreative();
    }

    public boolean isOp() {
        return this.entity.isCreativeLevelTwoOp();
    }

    public boolean isDisconnected() {
        return this.entity.isDisconnected();
    }

    public boolean isMainPlayer() {
        return this.entity.isMainPlayer();
    }

    public boolean isSpawnForced() {
        return this.entity.isSpawnForced();
    }

    public boolean isUsingSpyglass() {
        return this.entity.isUsingSpyglass();
    }
}

package com.cubecode.api.project.scripts.code.entities;

import com.cubecode.api.project.scripts.code.ScriptVector;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;


public class ScriptEntity<T extends Entity> {
    protected T entity;

    public static ScriptEntity<?> create(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return new ScriptPlayer((ServerPlayerEntity) entity);
        } else if (entity != null) {
            return new ScriptEntity<Entity>(entity);
        }

        return null;
    }

    protected ScriptEntity(T entity) {
        this.entity = entity;
    }

    public T getMinecraftEntity() {
        return entity;
    }



    public void addVelocity(ScriptVector vector) {
        this.entity.addVelocity(vector.toVec3d());
    }

    public void addVelocity(double deltaX, double deltaY, double deltaZ) {
        this.entity.addVelocity(deltaX, deltaY, deltaZ);
    }

    public void changeLookDirection(double deltaX, double deltaY) {
        this.entity.changeLookDirection(deltaX, deltaY);
    }

    public void dismountVehicle() {
        this.entity.dismountVehicle();
    }

    public void extinguish() {
        this.entity.extinguish();
    }

    public void extinguishWithSound() {
        this.entity.extinguishWithSound();
    }

    public void kill() {
        this.entity.kill();
    }

    public void lookAt(ScriptVector vector) {
        this.entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, vector.toVec3d());
    }

    public void lookAt(double x, double y, double z) {
        this.entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, new ScriptVector(x, y, z).toVec3d());
    }

    public void lookAtEntity(Entity entity) {
        this.entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getPos());
    }

    public void playSound(String sound, float volume, float pitch) {
        this.entity.playSound(SoundEvent.of(new Identifier(sound)), volume, pitch);
    }

    public void playSound(double x, double y, double z, String sound, String category, float volume, float pitch) {
        this.entity.getWorld().playSound(x, y, z, SoundEvent.of(new Identifier(sound)), SoundCategory.valueOf(category), volume, pitch, false);
    }

    public void playSound(ScriptVector vector, String sound, String category, float volume, float pitch) {
        this.entity.getWorld().playSound(vector.x, vector.y, vector.z, SoundEvent.of(new Identifier(sound)), SoundCategory.valueOf(category), volume, pitch, false);
    }

    public void playSound(double x, double y, double z, String sound, float volume, float pitch) {
        this.entity.getWorld().playSound(x, y, z, SoundEvent.of(new Identifier(sound)), SoundCategory.valueOf("master"), volume, pitch, false);
    }

    public void playSound(ScriptVector vector, String sound, float volume, float pitch) {
        this.entity.getWorld().playSound(vector.x, vector.y, vector.z, SoundEvent.of(new Identifier(sound)), SoundCategory.valueOf("master"), volume, pitch, false);
    }

    public void playSoundIfNotSilent(String sound) {
        this.entity.playSoundIfNotSilent(SoundEvent.of(new Identifier(sound)));
    }

    public void pushAwayFrom(Entity entity) {
        this.entity.pushAwayFrom(entity);
    }

    public void remove(String reason){
        this.entity.remove(Entity.RemovalReason.valueOf(reason));
    }

    public void removeAllPassengers() {
        this.entity.removeAllPassengers();
    }

    public void send(String message) {
        this.entity.sendMessage(Text.of(message));
    }

    public void setAir(int air) {
        this.entity.setAir(air);
    }

    public void setBodyYaw(float yaw) {
        this.entity.setBodyYaw(yaw);
    }

    public void setHeadYaw(float yaw) {
        this.entity.setHeadYaw(yaw);
    }

    public void setPitch(float pitch) {
        this.entity.setPitch(pitch);
    }

    public void setYaw(float yaw) {
        this.entity.setYaw(yaw);
    }

    public void setCustomName(String customName) {
        this.entity.setCustomName(Text.of(customName));
    }

    public void setNameVisible(boolean visible) {
        this.entity.setCustomNameVisible(visible);
    }

    public void setFireTicks(int ticks) {
        this.entity.setFireTicks(ticks);
    }

    public void setFrozenTicks(int ticks) {
        this.entity.setFrozenTicks(ticks);
    }

    public void setInPowderSnow(boolean inSnow) {
        this.entity.setInPowderSnow(inSnow);
    }

    public void setInvisible(boolean invisible) {
        this.entity.setInvisible(invisible);
    }

    public void setInvulnerable(boolean invulnerable) {
        this.entity.setInvulnerable(invulnerable);
    }

    public void setNoGravity(boolean noGravity) {
        this.entity.setNoGravity(noGravity);
    }

    public void setOnFire(boolean onFire) {
        this.entity.setOnFire(onFire);
    }


    public void setOnFireFor(int ticks) {
        this.entity.setOnFireFor(ticks);
    }

    public void setOnFireFromLava() {
        this.entity.setOnFireFromLava();
    }

    public void setOnGround(boolean onGround) {
        this.entity.setOnGround(onGround);
    }

    public void setPortalCooldown(int cooldown) {
        this.entity.setPortalCooldown(cooldown);
    }

    public void setPosition(double x, double y, double z) {
        this.entity.setPosition(x, y, z);
    }

    public void setPosition(ScriptVector vector) {
        this.entity.setPosition(vector.toVec3d());
    }

    public void setSilent(boolean silent) {
        this.entity.setSilent(silent);
    }

    public void setSneaking(boolean sneaking) {
        this.entity.setSneaking(sneaking);
    }

    public void setSprinting(boolean sprinting) {
        this.entity.setSprinting(sprinting);
    }
    public void setStepHeight(float stepHeight) {
        this.entity.setStepHeight(stepHeight);
    }

    public void setSwimming(boolean swimming) {
        this.entity.setSwimming(swimming);
    }

    public void setVelocity(double x, double y, double z) {
        this.entity.setPosition(x, y, z);
    }

    public void setVelocity(ScriptVector vector) {
        this.entity.setPosition(vector.toVec3d());
    }

    public void stopRiding() {
        this.entity.stopRiding();
    }
}

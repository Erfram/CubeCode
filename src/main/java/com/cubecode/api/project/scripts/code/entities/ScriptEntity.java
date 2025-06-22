package com.cubecode.api.project.scripts.code.entities;

import com.cubecode.api.project.scripts.code.ScriptVector;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;


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





    public boolean isTouchingRain() {
        BlockPos blockPos = this.entity.getBlockPos();
        return this.entity.getWorld().hasRain(blockPos) || this.entity.getWorld().hasRain(BlockPos.ofFloored((double)blockPos.getX(), this.entity.getBoundingBox().maxY, (double)blockPos.getZ()));
    }

    public boolean isInsideBubbleColumn() {
        return this.entity.getWorld().getBlockState(this.entity.getBlockPos()).isOf(Blocks.BUBBLE_COLUMN);
    }





    public boolean collidesWith(Entity other) {
        return this.entity.collidesWith(other);
    }

    public boolean hasCustomName() {
        return this.entity.hasCustomName();
    }

    public boolean hasNoGravity() {
        return this.entity.hasNoGravity();
    }

    public boolean hasPassenger(Entity passenger) {
        return this.entity.hasPassenger(passenger);
    }

    public boolean hasPassengerDeep(Entity passenger) {
        return this.entity.hasPassengerDeep(passenger);
    }

    public boolean hasPassengers() {
        return this.entity.hasPassengers();
    }

    public boolean hasPermissionLevel(int level) {
        return this.entity.hasPermissionLevel(level);
    }

    public boolean hasPlayerRider() {
        return this.entity.hasPlayerRider();
    }

    public boolean hasPortalCooldown() {
        return this.entity.hasPortalCooldown();
    }

    public boolean hasVehicle() {
        return this.entity.hasVehicle();
    }

    public boolean isAlive() {
        return this.entity.isAlive();
    }

    public boolean isAttackable() {
        return this.entity.isAttackable();
    }

    public boolean isConnectedThroughVehicle(Entity entity) {
        return this.entity.isConnectedThroughVehicle(entity);
    }

    public boolean isCrawling() {
        return this.entity.isCrawling();
    }

    public boolean isDescending() {
        return this.entity.isDescending();
    }

    public boolean isFireImmune() {
        return this.entity.isFireImmune();
    }

    public boolean isFrozen() {
        return this.entity.isFrozen();
    }

    public boolean isGlowing() {
        return this.entity.isGlowing();
    }

    public boolean isImmuneToExplosion() {
        return this.entity.isImmuneToExplosion();
    }

    public boolean isInLava() {
        return this.entity.isInLava();
    }

    public boolean isInRange(Entity entity, double horizontalRadius, double verticalRadius) {
        return this.entity.isInRange(entity, horizontalRadius, verticalRadius);
    }

    public boolean isInRange(Entity entity, double radius) {
        return this.entity.isInRange(entity, radius);
    }

    public boolean isInsideWall() {
        return this.entity.isInsideWall();
    }

    public boolean isInvisible() {
        return this.entity.isInvisible();
    }

    public boolean isInvisibleTo(PlayerEntity player) {
        return this.entity.isInvisibleTo(player);
    }

    public boolean isInvulnerable() {
        return this.entity.isInvulnerable();
    }

    public boolean isLiving() {
        return this.entity.isLiving();
    }

    public boolean isOnFire() {
        return this.entity.isOnFire();
    }

    public boolean isOnGround() {
        return this.entity.isOnGround();
    }

    public boolean isOnRail() {
        return this.entity.isOnRail();
    }

    public boolean isPlayer() {
        return this.entity.isPlayer();
    }

    public boolean isPushable() {
        return this.entity.isPushable();
    }

    public boolean isPushedByFluids() {
        return this.entity.isPushedByFluids();
    }

    public boolean isSilent() {
        return this.entity.isSilent();
    }

    public boolean isSneaking() {
        return this.entity.isSneaking();
    }

    public boolean isSneaky() {
        return this.entity.isSneaky();
    }

    public boolean isSpectator() {
        return this.entity.isSpectator();
    }

    public boolean isSprinting() {
        return this.entity.isSprinting();
    }

    public boolean isSubmergedInWater() {
        return this.entity.isSubmergedInWater();
    }

    public boolean isSupportedByBlock(ScriptVector pos) {
        return this.entity.isSupportedBy(pos.toBlockPos());
    }

    public boolean isSupportedByBlock(float x, float y, float z) {
        return this.entity.isSupportedBy(new ScriptVector(x, y, z).toBlockPos());
    }

    public boolean isSwimming() {
        return this.entity.isSwimming();
    }

    public boolean isTeammate(Entity other) {
        return this.entity.isTeammate(other);
    }

    public boolean isTouchingWater() {
        return this.entity.isTouchingWater();
    }

    public boolean isWet() {
        return this.entity.isWet();
    }

    public boolean startRiding(Entity entity) {
        return this.entity.startRiding(entity);
    }
}

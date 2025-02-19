package com.cubecode.api.scripts.code.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.cubecode.api.scripts.code.ScriptRayTrace;
import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.items.ScriptItemStack;

/**
 * Docs for entity bruh
 *
 * <pre>{@code
 * function main(c) {
 *     let player = c.getPlayer();
 *
 *     player.setPosition(224, 56, 87);
 *     player.setRotations(90, 180, 0);
 *
 *     c.getServer().send(\"Player \" + player.getName() + \" has changed his location.\", false);
 * }
 * }</pre>
 */
public class ScriptEntity<T extends Entity> {
    protected T entity;

    public static ScriptEntity create(Entity entity) {
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

    /**
     * Allows you to use minecraft mappings on an entity.
     *
     * <pre>{@code
     * //You can use yarn mappings
     * //All mappings search site https://linkie.shedaniel.dev/
     * c.getServer().send(c.getPlayer().getMinecraftEntity().getType(), false);
     * }</pre>
     */
    public Entity getMinecraftEntity() {
        return this.entity;
    }

    /**
     * Sets entity position in the world
     *
     * <pre>{@code
     * c.getPlayer().setPosition(485, 43, 43);
     * }</pre>
     */
    public void setPosition(double x, double y, double z) {
        this.entity.setPosition(x, y, z);
        if (this.entity instanceof LivingEntity) {
            this.entity.teleport(x, y, z);
        }
    }

    /**
     * Returns the entity's current position
     *
     * <pre>{@code
     * var pos = c.getPlayer().getPosition();
     *
     * c.getPlayer.setPosition(pos.x, pos.y, pos.z);
     * }</pre>
     */
    public ScriptVector getPosition() {
        return new ScriptVector(
                this.entity.getX(),
                this.entity.getY(),
                this.entity.getZ()
        );
    }

    /**
     * Sets the entity's rotation angles
     *
     * <pre>{@code
     * c.getPlayer().setRotations(90, 90, 0);
     * }</pre>
     */
    public void setRotations(float pitch, float yaw, float headYaw) {
        this.entity.setPitch(pitch);
        this.entity.setYaw(yaw);
        this.entity.setHeadYaw(headYaw);
    }

    /**
     * Sets the entity's rotation angles including body yaw
     *
     * <pre>{@code
     * c.getPlayer().setRotations(90, 90, 0, 0);
     * }</pre>
     */
    public void setRotations(float pitch, float yaw, float headYaw, float bodyYaw) {
        this.setRotations(pitch, yaw, headYaw);
        this.entity.setBodyYaw(bodyYaw);
    }

    /**
     * Returns the entity's current rotation angles
     *
     * <pre>{@code
     * var rot = c.getPlayer().getRotations();
     *
     * c.getPlayer().setRotations(rot.x, rot.y, rot.z);
     * }</pre>
     */
    public ScriptVector getRotations() {
        return new ScriptVector(
                this.entity.getPitch(),
                this.entity.getYaw(),
                this.entity.getHeadYaw()
        );
    }

    /**
     * Returns the entity's current body yaw angle
     */
    public float getBodyYaw() {
        return this.entity.getBodyYaw();
    }

    /**
     * Adds to the entity's current velocity
     *
     * <pre>{@code
     * c.getPlayer().addVelocity(0, 1, 0);
     * }</pre>
     */
    public void addVelocity(double x, double y, double z) {
        this.entity.addVelocity(x, y, z);
    }

    /**
     * Sets the entity's velocity
     *
     * <pre>{@code
     * c.getPlayer().setVelocity(0, 1, 0);
     * }</pre>
     */
    public void setVelocity(double x, double y, double z) {
        this.entity.setVelocity(x, y, z);
    }

    /**
     * Returns the entity's current velocity
     */
    public ScriptVector getVelocity() {
        return new ScriptVector(this.entity.getVelocity());
    }

    /**
     * Returns the world the entity is in
     */
    public ScriptWorld getWorld() {
        return new ScriptWorld(this.entity.getWorld());
    }

    /**
     * Returns the dimension the entity is in
     */
    public String getDimension() {
        return this.entity.getWorld().getDimension().toString();
    }

    /**
     * Makes the entity swing the specified hand
     *
     * <pre>{@code
     * c.getPlayer().swingHand("main");
     * }</pre>
     */
    public void swingHand(String hand) {
        ((LivingEntity)this.entity).swingHand(Hand.valueOf(hand.toUpperCase()+"_HAND"), true);
    }

    /**
     * Applies damage to the entity
     */
    public void damage(float damage) {
        this.entity.damage(this.entity.getDamageSources().generic(), damage);
    }

    /**
     * Returns the total distance the entity has traveled
     */
    public float getDistanceTraveled() {
        return this.entity.distanceTraveled;
    }

    /**
     * Returns the entity's unique identifier
     */
    public String getId() {
        return Registries.ENTITY_TYPE.getId(this.entity.getType()).toString();
    }

    /**
     * Returns the entity's name
     */
    public String getName() {
        return this.entity.getName().getLiteralString();
    }

    /**
     * Returns the direction the entity is facing
     */
    public String getFacing() {
        return this.entity.getHorizontalFacing().toString();
    }

    /**
     * Returns the entity's width
     */
    public float getWidth() {
        return this.entity.getWidth();
    }

    /**
     * Returns the entity's height
     */
    public float getHeight() {
        return this.entity.getHeight();
    }

    /**
     * Checks if the entity is a player
     */
    public boolean isPlayer() {
        return this.entity.isPlayer();
    }

    /**
     * Sets whether the entity is glowing
     */
    public void setGlowing(boolean isGlowing) {
        this.entity.setGlowing(isGlowing);
    }

    /**
     * Checks if the entity is glowing
     */
    public boolean isGlowing() {
        return this.entity.isGlowing();
    }

    /**
     * Returns the entity's movement speed
     */
    public float getMovementSpeed() {
        return ((LivingEntity)this.entity).getMovementSpeed();
    }

    /**
     * Sets the entity's movement speed
     */
    public void setMovementSpeed(float movementSpeed) {
        ((LivingEntity)this.entity).setMovementSpeed(movementSpeed);
    }

    /**
     * Returns the entity's rotation as a vector
     */
    public ScriptVector getRotationVector() {
        return new ScriptVector(this.entity.getRotationVector());
    }

    /**
     * Returns the entity's current health
     */
    public float getHealth() {
        return ((LivingEntity)this.entity).getHealth();
    }

    /**
     * Returns the entity's maximum health
     */
    public float getMaxHealth() {
        return ((LivingEntity)this.entity).getMaxHealth();
    }

    /**
     * Returns the entity's armor value
     */
    public int getArmor() {
        return ((LivingEntity)this.entity).getArmor();
    }

    /**
     * Sets whether the entity is sneaking
     */
    public void setSneaking(boolean sneaking) {
        this.entity.setSneaking(sneaking);
    }

    /**
     * Checks if the entity is sneaking
     */
    public boolean isSneaking() {
        return this.entity.isSneaking();
    }

    /**
     * Sets whether the entity is sprinting
     */
    public void setSprinting(boolean sprinting) {
        this.entity.setSprinting(sprinting);
    }

    /**
     * Checks if the entity is sprinting
     */
    public boolean isSprinting() {
        return this.entity.isSprinting();
    }

    /**
     * Checks if the entity is touching water
     */
    public boolean isTouchingWater() {
        return this.entity.isTouchingWater();
    }

    /**
     * Checks if the entity is in lava
     */
    public boolean isInLava() {
        return this.entity.isInLava();
    }

    /**
     * Checks if the entity is on fire
     */
    public boolean isBurning() {
        return this.entity.getFireTicks() > 0;
    }

    /**
     * Sets the entity on fire for the specified number of ticks
     */
    public void setBurning(int ticks) {
        if (ticks <= 0) {
            this.entity.extinguish();
        }
        else {
            this.entity.setFireTicks(ticks);
        }
    }

    /**
     * Checks if the entity is on the ground
     */
    public boolean isOnGround() {
        return this.entity.isOnGround();
    }

    /**
     * Kills the entity
     */
    public void kill() {
        this.entity.kill();
    }

    public void remove() {
        this.entity.remove(Entity.RemovalReason.DISCARDED);
    }

    /**
     * Returns the height of the entity's eyes above its feet
     */
    public double getEyeHeight() {
        return this.entity.getEyePos().y;
    }

    /**
     * Returns the position of the entity's eyes
     */
    public ScriptVector getEyePos() {
        return new ScriptVector(this.entity.getEyePos());
    }

    /**
     * Performs a ray trace for blocks from the entity's eyes
     *
     * <pre>{@code
     * function main(c) {
     *     let player = c.getPlayer();
     *     let rayTrace = player.rayTraceBlock(10, false);
     *
     *     c.getServer().send(rayTrace.getBlockSide(), false);
     * }
     * }</pre>
     */
    public ScriptRayTrace rayTraceBlock(double maxDistance, boolean includesFluids) {
        return new ScriptRayTrace(this.entity.raycast(maxDistance, 0, includesFluids));
    }

    /**
     * Performs a ray trace for entities from the entity's eyes
     *
     * <pre>{@code
     * function main(c) {
     *     let player = c.getPlayer();
     *     let rayTrace = player.rayTraceEntity(10);
     *
     *     c.getServer().send(rayTrace.getEntity().setVelocity(0, 1, 0), false);
     * }
     * }</pre>
     */
    public ScriptRayTrace rayTraceEntity(double maxDistance) {
        Vec3d eyePos = this.entity.getEyePos();
        Vec3d lookDir = this.entity.getRotationVec(1);
        Vec3d reach = lookDir.multiply(maxDistance);

        return new ScriptRayTrace(ProjectileUtil.raycast(
                this.entity,
                eyePos,
                eyePos.add(reach),
                this.entity.getBoundingBox().stretch(reach).expand(1, 1, 1),
                entity -> true,
                maxDistance * maxDistance
        ));
    }

    /**
     * Returns the entity's UUID
     */
    public String getUUID() {
        return this.entity.getUuidAsString();
    }

    /**
     * Returns the entity's type
     */
    public String getType() {
        return this.entity.getType().toString();
    }

    /**
     * Returns the item stack in the entity's main hand
     */
    public ScriptItemStack getMainItemStack() {
        return ScriptItemStack.create(((LivingEntity)this.entity).getMainHandStack());
    }

    /**
     * Returns the item stack in the entity's off hand
     */
    public ScriptItemStack getOffItemStack() {
        return ScriptItemStack.create(((LivingEntity)this.entity).getOffHandStack());
    }

    /**
     * Returns the direction the entity is looking as a vector
     *
     * <pre>{@code
     * function main(c) {
     *     let player = c.getPlayer();
     *     let look = player.getLook();
     *
     *     player.setVelocity(look.x, look.y, look.z);
     * }
     * }</pre>
     */
    public ScriptVector getLook() {
        float f1 = -((this.entity.getPitch()) * ((float)Math.PI / 180F));
        float f2 = (this.entity.getHeadYaw() * ((float)Math.PI / 180F));
        float f3 = -MathHelper.sin(f2);
        float f4 = MathHelper.cos(f2);
        float f6 = MathHelper.cos(f1);
        return new ScriptVector(f3 * f6, this.entity.getRotationVector().y, f4 * f6);
    }
}
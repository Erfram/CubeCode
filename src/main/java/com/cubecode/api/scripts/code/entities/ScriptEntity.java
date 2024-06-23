package com.cubecode.api.scripts.code.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.cubecode.api.scripts.code.ScriptRayTrace;
import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.items.ScriptItemStack;

public class ScriptEntity<T extends Entity> {
    protected T entity;

    public static ScriptEntity create(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return new ScriptPlayer((PlayerEntity) entity);
        } else if (entity != null) {
            return new ScriptEntity<Entity>(entity);
        }

        return null;
    }

    protected ScriptEntity(T entity) {
        this.entity = entity;
    }

    public T getMinecraftEntity() {
        return this.entity;
    }

    public void setPosition(double x, double y, double z) {
        this.entity.setPosition(x, y, z);
        if (this.entity instanceof LivingEntity) {
            this.entity.teleport(x, y, z);
        }
    }

    public ScriptVector getPosition() {
        return new ScriptVector(
                this.entity.getX(),
                this.entity.getY(),
                this.entity.getZ()
        );
    }

    public void setRotations(float pitch, float yaw, float headYaw) {
        this.entity.setPitch(pitch);
        this.entity.setYaw(yaw);
        this.entity.setHeadYaw(headYaw);
    }

    public void setRotations(float pitch, float yaw, float headYaw, float bodyYaw) {
        this.setRotations(pitch, yaw, headYaw);
        this.entity.setBodyYaw(bodyYaw);
    }

    public ScriptVector getRotations() {
        return new ScriptVector(
                this.entity.getPitch(),
                this.entity.getYaw(),
                this.entity.getHeadYaw()
        );
    }

    public float getBodyYaw() {
        return this.entity.getBodyYaw();
    }

    public void addVelocity(double x, double y, double z) {
        this.entity.addVelocity(x, y, z);
    }

    public void setVelocity(double x, double y, double z) {
        this.entity.setVelocity(x, y, z);
    }

    public ScriptVector getVelocity(double x, double y, double z) {
        return new ScriptVector(this.entity.getVelocity());
    }

    public ScriptWorld getWorld() {
        return new ScriptWorld(this.entity.getWorld());
    }

    public String getDimension() {
        return this.entity.getWorld().getDimension().toString();
    }

    public void swingHand(String hand) {
        ((LivingEntity)this.entity).swingHand(Hand.valueOf(hand.toUpperCase()+"_HAND"), true);
    }

    public void damage(float damage) {
        this.damage("generic", damage);
    }

    public void damage(String damageType, float damage) {
        this.entity.damage(this.entity.getDamageSources().create(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(damageType))), damage);
    }

    public float getDistanceTraveled() {
        return this.entity.distanceTraveled;
    }

    public String getId() {
        return Registries.ENTITY_TYPE.getId(this.entity.getType()).toString();
    }

    public String getName() {
        return this.entity.getName().getLiteralString();
    }

    public String getFacing() {
        return this.entity.getHorizontalFacing().toString();
    }

    public float getWidth() {
        return this.entity.getWidth();
    }

    public float getHeight() {
        return this.entity.getHeight();
    }

    public boolean isPlayer() {
        return this.entity.isPlayer();
    }

    public void setGlowing(boolean isGlowing) {
        this.entity.setGlowing(isGlowing);
    }

    public boolean isGlowing() {
        return this.entity.isGlowing();
    }

    public float getMovementSpeed() {
        return ((LivingEntity)this.entity).getMovementSpeed();
    }

    public void setMovementSpeed(float movementSpeed) {
        ((LivingEntity)this.entity).setMovementSpeed(movementSpeed);
    }

    public ScriptVector getRotationVector() {
        return new ScriptVector(this.entity.getRotationVector());
    }

    public float getHp() {
        return ((LivingEntity)this.entity).getHealth();
    }

    public float getMaxHp() {
        return ((LivingEntity)this.entity).getMaxHealth();
    }

    public int getArmor() {
        return ((LivingEntity)this.entity).getArmor();
    }

    public void setSneaking(boolean sneaking) {
        this.entity.setSneaking(sneaking);
    }

    public boolean isSneaking() {
        return this.entity.isSneaking();
    }

    public void setSprinting(boolean sprinting) {
        this.entity.setSprinting(sprinting);
    }

    public boolean isSprinting() {
        return this.entity.isSprinting();
    }

    public boolean isTouchingWater() {
        return this.entity.isTouchingWater();
    }

    public boolean isInLava() {
        return this.entity.isInLava();
    }

    public boolean isBurning() {
        return this.entity.getFireTicks() > 0;
    }

    public void setBurning(int ticks) {
        if (ticks <= 0) {
            this.entity.extinguish();
        }
        else {
            this.entity.setFireTicks(ticks);
        }
    }

    public boolean isOnGround() {
        return this.entity.isOnGround();
    }

    public void kill() {
        this.entity.kill();
    }

    public double getEyeHeight() {
        return this.entity.getEyePos().y;
    }

    public ScriptRayTrace rayTraceBlock(double maxDistance, boolean includesFluids) {
        return new ScriptRayTrace(this.entity.raycast(maxDistance, 0, includesFluids));
    }

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

    public String getUUID() {
        return this.entity.getUuidAsString();
    }

    public String getType() {
        return this.entity.getType().toString();
    }

    public ScriptItemStack getMainItemStack() {
        return ScriptItemStack.create(((LivingEntity)this.entity).getMainHandStack());
    }

    public ScriptItemStack getOffItemStack() {
        return ScriptItemStack.create(((LivingEntity)this.entity).getOffHandStack());
    }

    public ScriptVector getLook() {
        float f1 = -((this.entity.getPitch()) * ((float)Math.PI / 180F));
        float f2 = (this.entity.getHeadYaw() * ((float)Math.PI / 180F));
        float f3 = -MathHelper.sin(f2);
        float f4 = MathHelper.cos(f2);
        float f6 = MathHelper.cos(f1);
        return new ScriptVector(f3 * f6, this.entity.getRotationVector().y, f4 * f6);
    }
}
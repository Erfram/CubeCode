package com.cubecode.api.project.scripts.code.entities;

import com.cubecode.api.project.scripts.code.ScriptVector;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Set;


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



    //Добавляет вектор скорости к текущей скорости
    public void addVelocity(ScriptVector vector) {
        this.entity.addVelocity(vector.toVec3d());
        if (this.entity.isPlayer()) {
            this.entity.velocityModified = true;
        }
    }

    //Добавляет вектор скорости к текущей скорости
    public void addVelocity(float x, float y, float z) {
        this.entity.addVelocity(x, y, z);
        if (this.entity.isPlayer()) {
            this.entity.velocityModified = true;
        }
    }


    //Экипирует предмет на указанный слот (MAINHAND, OFFHAND, FEET, LEGS, CHEST, HEAD)
    public void equipItem(String slot, ItemStack itemStack) {
        this.entity.equipStack(EquipmentSlot.valueOf(slot.toUpperCase()), itemStack);
    }

    //Тушит огонь на сущности
    public void extinguish() {
        this.entity.extinguish();
    }

    //Убивает сущность
    public void kill() {
        this.entity.kill();
    }

    //Перемещает сущность
    public void move(ScriptVector vector) {
        this.entity.move(MovementType.SELF, vector.toVec3d());
    }

    //Перемещает сущность
    public void move(float x, float y, float z) {
        this.entity.move(MovementType.SELF, new Vec3d(x, y, z));
    }

    //Воспроизводит звук от сущности (если это игрок, он не услышит звук)
    public void playSound(String soundId, float volume, float pitch) {
        this.entity.playSound(SoundEvent.of(new Identifier(soundId)), volume, pitch);
    }

    //Удаляет сущность из мира
    public void remove() {
        if (!this.entity.isPlayer()) {
            this.entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    //Устанавливает количество воздуха у сущности
    public void setAir(int airCount) {
        this.entity.setAir(airCount);
    }

    //Устанавливает поворот тела сущности (не работает на игрока)
    public void setBodyYaw(float yaw) {
        this.entity.setBodyYaw(yaw);
    }

    //Устанавливает имя сущности (BUG: невозможно удалить кастомное имя)
    public void setCustomName(String name) {
        this.entity.setCustomName(Text.of(name));
    }

    //Устанавливает видимость имени
    public void setCustomNameVisible(boolean visible) {
        this.entity.setCustomNameVisible(visible);
    }

    //Устанавливает время горения сущности
    public void setFireTicks(int ticks) {
        this.entity.setFireTicks(ticks);
    }

    //Устанавливает время заморозки сущности
    public void setFrozenTicks(int ticks) {
        this.entity.setFrozenTicks(ticks);
    }

    //Устанавливает эффект свечения сущности
    public void setGlowing(boolean glowing) {
        this.entity.setGlowing(glowing);
    }

    //Устанавливает поворот головы сущности
    public void setHeadYaw(float yaw) {
        this.entity.setHeadYaw(yaw);
    }

    //Делает сущность невидимой
    public void setInvisible(boolean invisible) {
        this.entity.setInvisible(invisible);
    }

    //Делает сущность неуязвимой
    public void setInvulnerable(boolean invulnerable) {
        this.entity.setInvulnerable(invulnerable);
    }

    //Отключает гравитацию для сущности
    public void setNoGravity(boolean noGravity) {
        this.entity.setNoGravity(noGravity);
    }

    //Устанавливает наклон головы сущности
    public void setPitch(float pitch) {
        this.entity.setPitch(pitch);
    }

    //Устанавливает позицию сущности (не работает на игрока)
    public void setPosition(float x, float y, float z) {
        this.entity.setPosition(x, y, z);
    }

    //Устанавливает позицию сущности (не работает на игрока)
    public void setPosition(ScriptVector vector) {
        this.entity.setPosition(vector.toVec3d());
    }

    //Устанавливает режим бега
    public void setSprinting(boolean sprinting) {
        this.entity.setSprinting(sprinting);
    }

    //Устанавливает высоту шага сущности (не работает на игрока)
    public void setStepHeight(float stepHeight) {
        this.entity.setStepHeight(stepHeight);
    }

    //Устанавливает вектор скорости сущности
    public void setVelocity(float x, float y, float z) {
        this.entity.setVelocity(x, y, z);
        if (this.entity.isPlayer()) {
            this.entity.velocityModified = true;
        }
    }

    //Устанавливает вектор скорости сущности
    public void setVelocity(ScriptVector vector) {
        this.entity.setVelocity(vector.toVec3d());
        if (this.entity.isPlayer()) {
            this.entity.velocityModified = true;
        }
    }

    //Заставляет сущность слезть с транспорта (если bool равен true, сущьность остаётся на той-же позиции)
    public void stopRiding(boolean savePos) {
        if (savePos) {
            Vec3d vehiclePos = this.entity.getRootVehicle().getPos();
            this.entity.dismountVehicle();
            this.entity.setPos(vehiclePos.x, vehiclePos.y, vehiclePos.z);
        } else {
            this.entity.stopRiding();
        }
    }

    //Проверяет наличие пользовательского имени
    public boolean hasCustomName() {
        return this.entity.hasCustomName();
    }

    //Проверяет отсутствие гравитации
    public boolean hasNoGravity() {
        return this.entity.hasNoGravity();
    }

    //Проверяет наличие пассажира
    public boolean hasPassenger(Entity passenger) {
        return this.entity.hasPassenger(passenger);
    }

    //Проверяет наличие пассажира на любом уровне
    public boolean hasPassengerDeep(Entity passenger) {
        return this.entity.hasPassengerDeep(passenger);
    }

    //Проверяет наличие пассажиров
    public boolean hasPassengers() {
        return this.entity.hasPassengers();
    }

    //Проверяет уровень разрешений ( 0 "all", 1 "moderator", 2 "gamemaster", 3 "admin", 4 "owner" )
    public boolean hasPermissionLevel(int level) {
        return this.entity.hasPermissionLevel(level);
    }

    //Проверяет нахождение в рыхлом снеге
    public boolean inPowderSnow() {
        return this.entity.inPowderSnow;
    }

    //Проверяет, жива ли сущность
    public boolean isAlive() {
        return this.entity.isAlive();
    }

    //Проверяет, ползает ли сущность
    public boolean isCrawling() {
        return this.entity.isCrawling();
    }

    //Проверяет видимость пользовательского имени
    public boolean isCustomNameVisible() {
        return this.entity.isCustomNameVisible();
    }

    //Проверяет иммунитет к огню
    public boolean isFireImmune() {
        return this.entity.isFireImmune();
    }

    //Проверяет, заморожена ли сущность
    public boolean isFrozen() {
        return this.entity.isFrozen();
    }

    //Проверяет, светится ли сущность
    public boolean isGlowing() {
        return this.entity.isGlowing();
    }

    //Проверяет нахождение в лаве
    public boolean isInLava() {
        return this.entity.isInLava();
    }

    //Проверяет нахождение внутри стены
    public boolean isInsideWall() {
        return this.entity.isInsideWall();
    }

    //Проверяет невидимость сущности
    public boolean isInvisible() {
        return this.entity.isInvisible();
    }

    //Проверяет неуязвимость сущности
    public boolean isInvulnerable() {
        return this.entity.isInvulnerable();
    }

    //Проверяет, горит ли сущность
    public boolean isOnFire() {
        return this.entity.isOnFire();
    }

    //Проверяет, на земле ли сущность
    public boolean isOnGround() {
        return this.entity.isOnGround();
    }

    //Проверяет нахождение на рельсах
    public boolean isOnRail() {
        return this.entity.isOnRail();
    }

    //Проверяет, является ли сущность игроком
    public boolean isPlayer() {
        return this.entity.isPlayer();
    }

    //Проверяет, можно ли толкать сущность
    public boolean isPushable() {
        return this.entity.isPushable();
    }

    //Проверяет, толкается ли сущность жидкостями
    public boolean isPushedByFluids() {
        return this.entity.isPushedByFluids();
    }

    //Проверяет, удалена ли сущность
    public boolean isRemoved() {
        return this.entity.isRemoved();
    }

    //Проверяет, беззвучна ли сущность
    public boolean isSilent() {
        return this.entity.isSilent();
    }

    //Проверяет режим подкрадывания
    public boolean isSneaking() {
        return this.entity.isSneaking();
    }

    //Проверяет, является ли сущность зрителем
    public boolean isSpectator() {
        return this.entity.isSpectator();
    }

    //Проверяет режим бега
    public boolean isSprinting() {
        return this.entity.isSprinting();
    }

    //Проверяет погружение в воду
    public boolean isSubmergedInWater() {
        return this.entity.isSubmergedInWater();
    }

    //Проверяет режим плавания
    public boolean isSwimming() {
        return this.entity.isSwimming();
    }

    //Проверяет принадлежность к команде
    public boolean isTeamPlayer() {
        return this.entity.getScoreboardTeam() != null;
    }

    //Проверяет, является ли сущьность товарищем по команде
    public boolean isTeammate(Entity entity) {
        return this.entity.isTeammate(entity);
    }

    //Проверяет контактирует ли сущьность с водой
    public boolean isTouchingWater() {
        return this.entity.isTouchingWater();
    }

    //Начинает езду на транспорте/сущьности
    public boolean startRiding(Entity entity) {
        return this.entity.startRiding(entity);
    }

    //Проверяет соприкосновение сущьности с пузырьками в воде
    public boolean isInsideBubbleColumn() {
        return this.entity.getWorld().getBlockState(this.entity.getBlockPos()).isOf(Blocks.BUBBLE_COLUMN);
    }

    //Проверяет погружена ли сущьность в лаву
    public boolean isSubmergedInLava() {
        return this.entity.isSubmergedIn(FluidTags.LAVA);
    }

    //Проверяет контактирует ли сущьность с лавой
    public boolean isTouchingLava() {
        return this.entity.updateMovementInFluid(FluidTags.LAVA, 0);
    }

    //Проверяет контактирует ли сущьность с дождём
    public boolean isTouchingRain() {
        BlockPos blockPos = this.entity.getBlockPos();
        return this.entity.getWorld().hasRain(blockPos) || this.entity.getWorld().hasRain(BlockPos.ofFloored((double)blockPos.getX(), this.entity.getBoundingBox().maxY, (double)blockPos.getZ()));
    }

    //Возвращает возраст сущности в тиках
    public int getAge() {
        return this.entity.age;
    }

    //Возвращает количество воздуха у сущности
    public int getAir() {
        return this.entity.getAir();
    }

    //Возвращает тики горения сущности
    public int getFireTicks() {
        return this.entity.getFireTicks();
    }

    //Возвращает тики заморозки сущности
    public int getFrozenTicks() {
        return this.entity.getFrozenTicks();
    }

    //Возвращает максимальное количество воздуха сущности
    public int getMaxAir() {
        return this.entity.getMaxAir();
    }

    //Возвращает дистанцию до другой сущности
    public float distanceTo(Entity entity) {
        return this.entity.distanceTo(entity);
    }

    //Пройденная дистанция
    public float getDistanceTraveled() {
        return this.entity.distanceTraveled;
    }

    //Дистанция падения
    public float getFallDistance() {
        return this.entity.fallDistance;
    }

    //Возвращает поворот тела
    public float getBodyYaw() {
        return this.entity.getBodyYaw();
    }

    //Возвращает высоту глаз (если true Возвращает высоту глаз в стоячем положении)
    public float getEyeHeight(boolean stock) {
        return stock ? this.entity.getStandingEyeHeight() : this.entity.getEyeHeight(this.entity.getPose());
    }

    //Возвращает масштаб заморозки
    public float getFreezingScale() {
        return this.entity.getFreezingScale();
    }

    //Возвращает поворот головы
    public float getHeadYaw() {
        return this.entity.getHeadYaw();
    }

    //Возвращает высоту сущности
    public float getHeight() {
        return this.entity.getHeight();
    }

    //Возвращает наклон головы сущности
    public float getPitch() {
        return this.entity.getPitch();
    }

    //Возвращает высоту шага
    public float getStepHeight() {
        return this.entity.getStepHeight();
    }

    //Возвращает имя сущности
    public String getEntityName() {
        return this.entity.getEntityName();
    }

    //Возвращает имя сущности
    public String getName() {
        return this.entity.getName().getString();
    }

    //Возвращает команду игрока (требует доработки)
    public AbstractTeam getTeam() {
        return this.entity.getScoreboardTeam();
    }

    //Возвращает источники урона (требует добаботки)
    public DamageSources getDamageSources(String damageType) {
        return this.entity.getDamageSources();
    }

    //Возвращает корневой транспорт
    public Entity getRootVehicle() {
        return this.entity.getRootVehicle();
    }

    //
    public Entity getVehicle() {
        return this.entity.getVehicle();
    }

    //Выбрасывает предмет (создаёт его)
    public ItemEntity dropItem(String id) {
        return this.entity.dropItem(Registries.ITEM.get(new Identifier(id)));
    }

    //Выбрасывает стопку предметов (создаёт его)
    public ItemEntity dropStack(String id, int count) {
        return this.entity.dropStack(new ItemStack(Registries.ITEM.get(new Identifier(id)), count));
    }

    //Возвращает предметы брони
    public Iterable<ItemStack> getArmorItems() {
        return this.entity.getArmorItems();
    }

    //Возвращает предметы в руках
    public Iterable<ItemStack> getHandItems() {
        return this.entity.getHandItems();
    }

    //Возвращает экипированные предметы
    public Iterable<ItemStack> getItemsEquipped() {
        return this.entity.getItemsEquipped();
    }

    //Возвращает список пассажиров
    public List<Entity> getPassengerList() {
        return this.entity.getPassengerList();
    }

    //Возвращает сервер Minecraft
    public MinecraftServer getServer() {
        return this.entity.getServer(); 
    }

    //Опциональная позиция поддерживающего блока
    public Optional<BlockPos> supportingBlockPos() {
        return this.entity.supportingBlockPos;
    }

    //Возвращает набор тегов
    public Set<String> getTags() {
        return this.entity.getCommandTags();
    }

    //Возвращает позицию сущьности
    public ScriptVector getPosition() {
        return new ScriptVector(this.entity.getPos());
    }

    // Возвращает вектор скорости
    public ScriptVector getVelocity() {
        return new ScriptVector(this.entity.getVelocity());
    }

    //Возвращает мир сущности
    public World getEntityWorld() {
        return this.entity.getEntityWorld();
    }

    //Возвращает мир
    public World getWorld() {
        return this.entity.getWorld();
    }
}






































































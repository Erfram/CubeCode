package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.code.blocks.ScriptBlockEntity;
import com.mojang.brigadier.StringReader;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import com.cubecode.api.scripts.code.blocks.ScriptBlockState;
import com.cubecode.api.scripts.code.entities.ScriptEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Tab with methods that can be used on world.
 *
 * <pre>{@code
 * var block = c.getWorld().getBlock(213, 24, 232);
 *
 * c.getWorld().setBlock(block, 232, 55, 563);
 * }</pre>
 */
public class ScriptWorld {
    private World world;

    public ScriptWorld(World world) {
        this.world = world;
    }

    /**
     * Returns the Minecraft World object associated with this script world
     *
     * <pre>{@code
     * //You can use yarn mappings
     * //All mappings search site https://linkie.shedaniel.dev/
     *
     * c.getWorld().getMinecraftWorld().getWorldChunk(CubeCode.vector(232, 232, 223).toBlockPos());
     * }</pre>
     */
    public World getMinecraftWorld() {
        return this.world;
    }

    /**
     * Returns the block state at the specified coordinates
     */
    public ScriptBlockState getBlock(int x, int y, int z) {
        return ScriptBlockState.create(this.world.getBlockState(new BlockPos(x, y, z)));
    }

    /**
     * Returns the block state at the specified vector position
     */
    public ScriptBlockState getBlock(ScriptVector vector) {
        return ScriptBlockState.create(this.world.getBlockState(vector.toBlockPos()));
    }

    /**
     * getBlockEntity
     */
    public ScriptBlockEntity getBlockEntity(int x, int y, int z) {
        return new ScriptBlockEntity(this.world.getBlockEntity(new BlockPos(x, y, z)));
    }

    /**
     * getBlockEntity
     */
    public ScriptBlockEntity getBlockEntity(ScriptVector vector) {
        return new ScriptBlockEntity(this.world.getBlockEntity(vector.toBlockPos()));
    }

    /**
     * Sets the block at the specified coordinates to the given block state
     *
     * <pre>{@code
     * c.getWorld().setBlock(CubeCode.createBlockState("minecraft:dirt"), 223, 223, 223);
     * }</pre>
     */
    public void setBlock(ScriptBlockState scriptBlockState, int x, int y, int z) {
        this.world.setBlockState(new BlockPos(x, y, z), scriptBlockState.getMinecraftBlockState());
    }

    /**
     * Sets the block at the specified vector position to the given block state
     *
     * <pre>{@code
     * var pos = CubeCode.vector(223, 223, 223);
     *
     * c.getWorld().setBlock(CubeCode.createBlockState("minecraft:dirt"), pos.x, pos.y, pos.z);
     * }</pre>
     */
    public void setBlock(ScriptBlockState scriptBlockState, ScriptVector vector) {
        this.world.setBlockState(vector.toBlockPos(), scriptBlockState.getMinecraftBlockState());
    }

    /**
     * setBlockEntity
     */
    public void setBlockEntity(ScriptBlockEntity scriptBlockEntity, int x, int y, int z) {
        this.world.setBlockState(new BlockPos(x, y, z), scriptBlockEntity.getBlock().getMinecraftBlockState());
    }


    /**
     * setBlockEntity
     */
    public void setBlockEntity(ScriptBlockEntity scriptBlockEntity, ScriptVector vector) {
        this.world.setBlockState(vector.toBlockPos(), scriptBlockEntity.getBlock().getMinecraftBlockState());
    }

    /**
     * Returns the server associated with this world
     */
    public ScriptServer getServer() {
        return new ScriptServer(this.world.getServer());
    }

    /**
     * Returns a list of entities that match the given selector
     *
     * <pre>{@code
     * var entities = c.getWorld().getEntities("@e[type=minecraft:cow]");
     *
     * entities.forEach(entity => entity.setVelocity(0, 1, 0));
     * }</pre>
     */
    public List<ScriptEntity<? extends Entity>> getEntities(String selector) {
        List<ScriptEntity<?>> entities = new ArrayList<>();

        try {
            EntitySelector entitySelector = new EntitySelectorReader(new StringReader(selector)).read();

            entitySelector.getEntities(this.world.getServer().getCommandSource()).forEach((entity -> entities.add(ScriptEntity.create(entity))));
        } catch (Exception ignored) {}

        return entities;
    }

    /**
     * Returns the entity with the specified UUID
     *
     * <pre>{@code
     * c.getWorld().getEntity("5c5d80c8-7583-47f9-bb0e-de13fc816ad5");
     * }</pre>
     */
    public ScriptEntity<?> getEntity(String uuid) {
        return ScriptEntity.create(((ServerWorld)this.world).getEntity(UUID.fromString(uuid)));
    }

    /**
     * Spawns an entity of the specified type at the given coordinates
     *
     * <pre>{@code
     * c.getWorld().spawnEntity("minecraft:cow", 232, 20, 232);
     * }</pre>
     */
    public void spawnEntity(String id, double x, double y, double z) {
        Registries.ENTITY_TYPE.get(new Identifier(id)).spawn((ServerWorld) this.world, new BlockPos((int) x, (int) y, (int) z), SpawnReason.COMMAND);
    }

    /**
     * Plays a sound at the specified location with custom parameters
     *
     * <pre>{@code
     * c.getWorld().playSound(245, 45, 323, "block.anvil.break", "master", 1, 1, true);
     * }</pre>
     */
    public void playSound(double x, double y, double z, String soundEvent, String soundCategory, float volume, float pitch, boolean useDistance) {
        this.world.playSound(x, y, z, SoundEvent.of(new Identifier(soundEvent)), SoundCategory.valueOf(soundCategory), volume, pitch, useDistance);
    }


    /**
     * Plays a sound at the specified location with default parameters
     *
     * <pre>{@code
     * c.getWorld().playSound(245, 45, 323, "block.anvil.break", "master");
     * }</pre>
     */
    public void playSound(double x, double y, double z, String soundEvent, String soundCategory) {
        this.world.playSound(null, x, y, z, SoundEvent.of(new Identifier(soundEvent)), SoundCategory.valueOf(soundCategory));
    }

    /**
     * time
     */
    public long getTime() {
        return this.world.getTime();
    }

    /**
     * Returns a list of entities within the specified bounding box
     *
     * <pre>{@code
     * var cows = c.getWorld().getEntities("@e[type=minecraft:cow]", 211, 213, 123, 311, 210, 120)
     *
     * cows.forEach(entity => entity.setVelocity(0, 1, 0));
     * }</pre>
     */
    public List<ScriptEntity<?>> getEntities(ScriptEntity<?> entity, double x1, double y1, double z1, double x2, double y2, double z2) {
        List<ScriptEntity<?>> scriptEntities = new ArrayList<>();

        this.world.getOtherEntities(entity.getMinecraftEntity(), new Box(x1, y1, z1, x2, y2, z2)).forEach((otherEntity -> {
            scriptEntities.add(ScriptEntity.create(otherEntity));
        }));

        return scriptEntities;
    }

    /**
     * Returns a list of entities within the bounding box defined by two vectors
     *
     * <pre>{@code
     * c.getWorld().getEntities(
     *      "@e[type=minecraft:cow]",
     *      CubeCode.vector(211, 213, 123),
     *      CubeCode.vector(311, 210, 120)
     * ).forEach(entity => entity.setVelocity(0, 1, 0););
     * }</pre>
     */
    public List<ScriptEntity<?>> getEntities(ScriptEntity<?> entity, ScriptVector vector1, ScriptVector vector2) {
        return this.getEntities(entity, vector1.x, vector1.y, vector1.z, vector2.x, vector2.y, vector2.z);
    }
}
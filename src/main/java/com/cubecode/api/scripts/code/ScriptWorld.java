package com.cubecode.api.scripts.code;

import com.mojang.brigadier.StringReader;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
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

public class ScriptWorld {
    private World world;

    public ScriptWorld(World world) {
        this.world = world;
    }

    public World getMinecraftWorld() {
        return this.world;
    }

    public ScriptBlockState getBlock(int x, int y, int z) {
        return ScriptBlockState.create(this.world.getBlockState(new BlockPos(x, y, z)));
    }

    public ScriptBlockState getBlock(ScriptVector vector) {
        return ScriptBlockState.create(this.world.getBlockState(vector.toBlockPos()));
    }

    public void setBlock(ScriptBlockState scriptBlockState, int x, int y, int z) {
        this.world.setBlockState(new BlockPos(x, y, z), scriptBlockState.getMinecraftBlockState());
    }

    public void setBlock(ScriptBlockState scriptBlockState, ScriptVector vector) {
        this.world.setBlockState(vector.toBlockPos(), scriptBlockState.getMinecraftBlockState());
    }

    public ScriptServer getServer() {
        return new ScriptServer(this.world.getServer());
    }

    public List<ScriptEntity<?>> getEntities(String selector) {
        List<ScriptEntity<?>> entities = new ArrayList<>();

        try {
            EntitySelector entitySelector = new EntitySelectorReader(new StringReader(selector)).read();

            entitySelector.getEntities(this.world.getServer().getCommandSource()).forEach((entity -> entities.add(ScriptEntity.create(entity))));
        } catch (Exception ignored) {}

        return entities;
    }

    public ScriptEntity<?> getEntity(String uuid) {
        return ScriptEntity.create(((ServerWorld)this.world).getEntity(UUID.fromString(uuid)));
    }

    public void spawnEntity(String id, double x, double y, double z) {
        Registries.ENTITY_TYPE.get(new Identifier(id)).spawn((ServerWorld) this.world, new BlockPos((int) x, (int) y, (int) z), SpawnReason.COMMAND);
    }

    public void playSound(double x, double y, double z, String soundEvent, String soundCategory, float volume, float pitch, boolean useDistance) {
        this.world.playSound(x, y, z, SoundEvent.of(new Identifier(soundEvent)), SoundCategory.valueOf(soundCategory), volume, pitch, useDistance);
    }

    public void playSound(double x, double y, double z, String soundEvent, String soundCategory) {
        this.world.playSound(null, x, y, z, SoundEvent.of(new Identifier(soundEvent)), SoundCategory.valueOf(soundCategory));
    }

    public long getTime() {
        return this.world.getTime();
    }

    public List<ScriptEntity> getEntities(ScriptEntity entity, double x1, double y1, double z1, double x2, double y2, double z2) {
        List<ScriptEntity> scriptEntities = new ArrayList<>();

        this.world.getOtherEntities(entity.getMinecraftEntity(), new Box(x1, y1, z1, x2, y2, z2)).forEach((otherEntity -> {
            scriptEntities.add(ScriptEntity.create(otherEntity));
        }));

        return scriptEntities;
    }

    public void getEntities(ScriptEntity entity, ScriptVector vector1, ScriptVector vector2) {
        this.getEntities(entity, vector1.x, vector1.y, vector1.z, vector2.x, vector2.y, vector2.z);
    }
}
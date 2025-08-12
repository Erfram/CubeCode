package com.cubecode.api.project.scripts.code;

import com.cubecode.api.project.scripts.code.entities.ScriptEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;

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

    public Entity getEntity(String uuid) {
        return ((ServerWorld)this.world).getEntity(UUID.fromString(uuid));

    }



}
































































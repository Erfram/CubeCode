package com.cubecode.api.project.scripts.code;

import net.minecraft.world.World;

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
}

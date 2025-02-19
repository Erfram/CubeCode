package com.cubecode.api.scripts.code.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.utils.CubeCodeException;

import java.util.ArrayList;
import java.util.List;

/**
 * ScriptBlockState represents the state of a block in the Minecraft world, including its properties, characteristics, and behavior parameters.
 *
 * <pre>{@code
 * c.getServer().send(CubeCode.createBlockState(\"minecraft:dirt\").getId(), false)
 * }</pre>
 */
public class ScriptBlockState {
    public static final BlockState AIR = Blocks.AIR.getDefaultState();

    private BlockState blockState;

    public static ScriptBlockState create(BlockState blockState)
    {
        if (blockState == AIR || blockState == null) {
            return new ScriptBlockState(AIR);
        }

        return new ScriptBlockState(blockState);
    }

    private ScriptBlockState(BlockState state) {
        this.blockState = state;
    }

    /**
     * Returns the underlying Minecraft BlockState instance.
     *
     * This method provides access to the native Minecraft block state object,
     * useful for advanced operations or compatibility with other mods.
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(1, 1, 1);
     * const minecraftState = blockState.getMinecraftBlockState();
     * }</pre>
     */
    public BlockState getMinecraftBlockState() {
        return this.blockState;
    }

    /**
     * Returns the identifier of this block state.
     *
     * Returns the block ID in the format "namespace:name" (e.g., "minecraft:stone").
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(x, y, z);
     * const blockId = blockState.getId();
     * c.server.send(`Block ID: ${blockId}`, false);
     * }</pre>
     */
    public String getId() {
        return Registries.BLOCK.getId(this.blockState.getBlock()).toString();
    }

    /**
     * Checks if this block state is exactly the same as another block state.
     * Compares both the block type and all state properties.
     *
     * <pre>{@code
     * CubeCode.createBlockState(\"minecraft:dirt\").isSame(CubeCode.createBlockState(\"minecraft:dirt\"));
     * }</pre>
     */
    public boolean isSame(ScriptBlockState state) {
        return this.equals(state);
    }


    /**
     * Checks if this block state is of the same block type as another state.
     *
     * Compares only the block type, ignoring state properties.
     *
     * <pre>{@code
     * const block1 = c.world.getBlock(x1, y1, z1);
     * const block2 = c.world.getBlock(x2, y2, z2);
     * if (block1.isSameBlock(block2)) {
     *     c.server.send("The blocks are of the same type", false);
     * }
     * }</pre>
     */
    public boolean isSameBlock(ScriptBlockState state) {
        return this.blockState.getBlock() == state.getMinecraftBlockState().getBlock();
    }

    /**
     * Checks if this block is opaque (fully blocks light).
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(x, y, z);
     * if (blockState.isOpaque()) {
     *     c.server.send("This block completely blocks light", false);
     * }
     * }</pre>
     */
    public boolean isOpaque() {
        return this.blockState.isOpaque();
    }

    /**
     * Checks if this block state represents air.
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(x, y, z);
     * if (blockState.isAir()) {
     *     c.server.send("This block is air", false);
     * }
     * }</pre>
     */
    public boolean isAir() {
        return this.blockState.isAir();
    }

    /**
     * Checks if this block can be burned.
     */
    public boolean isBurnable() {
        return this.blockState.isBurnable();
    }

    /**
     * Checks if this block has collision at the specified position.
     */
    public boolean hasCollision(ScriptWorld world, int x, int y, int z) {
        return this.blockState.getCollisionShape(world.getMinecraftWorld(), new BlockPos(x, y, z)) != null;
    }

    /**
     * Returns a list of available property names for this block state.
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(x, y, z);
     * const properties = blockState.getProperties();
     * properties.forEach(prop => {
     *     c.server.send(`Property: ${prop}`, false);
     * });
     * }</pre>
     */
    public List<String> getProperties() {
        List<String> properties = new ArrayList<>();

        this.blockState.getProperties().forEach((p) -> {
            properties.add(p.getName());
        });

        return properties;
    }

    /**
     * Returns the name of this block.
     */
    public String getName() {
        return this.blockState.getBlock().getName().toString();
    }

    /**
     * Returns the blast resistance value of this block.
     */
    public float getBlastResistance() {
        return this.blockState.getBlock().getBlastResistance();
    }

    /**
     * Returns the slipperiness value of this block.
     *
     * Higher values make entities slide more on the block.
     */
    public float getSlipperiness() {
        return this.blockState.getBlock().getSlipperiness();
    }

    /**
     * Returns the hardness value of this block.
     *
     * Determines how long it takes to break the block.
     */
    public float getHardness() {
        return this.blockState.getBlock().getHardness();
    }

    /**
     * Returns the velocity multiplier for entities moving on this block.
     */
    public float getVelocityMultiplier() {
        return this.blockState.getBlock().getVelocityMultiplier();
    }

    /**
     * Returns the jump velocity multiplier for entities jumping from this block.
     */
    public float getJumpVelocityMultiplier() {
        return this.blockState.getBlock().getJumpVelocityMultiplier();
    }

    /**
     * Gets the value of a specific block state property.
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(x, y, z);
     * if (blockState.getId() === "minecraft:door") {
     *     const isOpen = blockState.getProperty("open");
     *     c.server.send(`Door is open: ${isOpen}`, false);
     * }
     * }</pre>
     */
    public Object getProperty(String name) throws CubeCodeException {
        return this.blockState.getProperties().stream()
                .filter(property -> property.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(property -> this.blockState.get(property))
                .orElseThrow(() -> new CubeCodeException("Unknown property: " + name));
    }

    /**
     * Sets the value of a specific block state property.
     *
     * <pre>{@code
     * const blockState = c.world.getBlock(x, y, z);
     * if (blockState.getId() === "minecraft:door") {
     *     blockState.setProperty("open", true);
     * }
     * }</pre>
     */
    public void setProperty(String name, Object value) throws CubeCodeException {
        if (!(value instanceof Comparable)) {
            throw new CubeCodeException("Not a valid property value: " + value);
        } else {
            Property property = getPropertyByName(name);

            if(value instanceof String) {
                this.setPropertyValueEnum(property, (Comparable) value);
            } else {
                this.setPropertyValue(property, (Comparable)value);
            }
        }
    }

    private Property<?> getPropertyByName(String name) throws CubeCodeException {
        for (Property<?> property : this.blockState.getProperties()) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }
        throw new CubeCodeException("Unknown property: " + name);
    }

    private <T extends Comparable<T>, E extends Enum<E>> void setPropertyValueEnum(Property<E> property, T value) {
        this.blockState = this.blockState.with(property, Enum.valueOf(property.getType(), ((String) value).toUpperCase()));
    }

    private <T extends Comparable<T>> void setPropertyValue(Property<T> property, T value) {
        this.blockState = this.blockState.with(property, property.getType().cast(value));
    }
}

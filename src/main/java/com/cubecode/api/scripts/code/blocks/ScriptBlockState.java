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
     * Returns the current Minecraft block state
     *
     * <pre>{@code
     * CubeCode.createBlockState(\"minecraft:dirt\").getMinecraftBlockState();
     * }</pre>
     */
    public BlockState getMinecraftBlockState() {
        return this.blockState;
    }

    /**
     * Returns the identifier of the block state
     */
    public String getId() {
        return Registries.BLOCK.getId(this.blockState.getBlock()).toString();
    }

    /**
     * Checks if this block state is the same as the given state
     *
     * <pre>{@code
     * CubeCode.createBlockState(\"minecraft:dirt\").isSame(CubeCode.createBlockState(\"minecraft:dirt\"));
     * }</pre>
     */
    public boolean isSame(ScriptBlockState state) {
        return this.equals(state);
    }


    /**
     * Checks if this block state is from the same block as the given state
     *
     * <pre>{@code
     *      CubeCode.createBlockState(\"minecraft:dirt\").isSameBlock(CubeCode.createBlockState(\"minecraft:dirt\"));
     * }</pre>
     */
    public boolean isSameBlock(ScriptBlockState state) {
        return this.blockState.getBlock() == state.getMinecraftBlockState().getBlock();
    }

    /**
     * Checks if the block state is opaque
     */
    public boolean isOpaque() {
        return this.blockState.isOpaque();
    }

    /**
     * Checks if the block state represents air
     */
    public boolean isAir() {
        return this.blockState.isAir();
    }

    /**
     * Checks if the block state is burnable
     */
    public boolean isBurnable() {
        return this.blockState.isBurnable();
    }

    /**
     * Checks if the block state has collision at the given coordinates in the world
     */
    public boolean hasCollision(ScriptWorld world, int x, int y, int z) {
        return this.blockState.getCollisionShape(world.getMinecraftWorld(), new BlockPos(x, y, z)) != null;
    }

    /**
     * Returns a list of property names for the block state
     */
    public List<String> getProperties() {
        List<String> properties = new ArrayList<>();

        this.blockState.getProperties().forEach((p) -> {
            properties.add(p.getName());
        });

        return properties;
    }

    /**
     * Returns the name of the block
     */
    public String getName() {
        return this.blockState.getBlock().getName().toString();
    }

    /**
     * Returns the blast resistance of the block
     */
    public float getBlastResistance() {
        return this.blockState.getBlock().getBlastResistance();
    }

    /**
     * Returns the slipperiness of the block
     */
    public float getSlipperiness() {
        return this.blockState.getBlock().getSlipperiness();
    }

    /**
     * Returns the hardness of the block
     */
    public float getHardness() {
        return this.blockState.getBlock().getHardness();
    }

    /**
     * Returns the velocity multiplier of the block
     */
    public float getVelocityMultiplier() {
        return this.blockState.getBlock().getVelocityMultiplier();
    }

    /**
     * Returns the jump velocity multiplier of the block
     */
    public float getJumpVelocityMultiplier() {
        return this.blockState.getBlock().getJumpVelocityMultiplier();
    }

    /**
     * 	Returns the value of the specified property
     */
    public Object getProperty(String name) throws CubeCodeException {
        return this.blockState.getProperties().stream()
                .filter(property -> property.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(property -> this.blockState.get(property))
                .orElseThrow(() -> new CubeCodeException("Unknown property: " + name));
    }

    /**
     * Sets the value of the specified property
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

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

    public BlockState getMinecraftBlockState() {
        return this.blockState;
    }

    public String getId() {
        return Registries.BLOCK.getId(this.blockState.getBlock()).toString();
    }

    public boolean isSame(ScriptBlockState state) {
        return this.equals(state);
    }

    public boolean isSameBlock(ScriptBlockState state) {
        return this.blockState.getBlock() == state.getMinecraftBlockState().getBlock();
    }

    public boolean isOpaque() {
        return this.blockState.isOpaque();
    }

    public boolean isAir() {
        return this.blockState.isAir();
    }

    public boolean isBurnable() {
        return this.blockState.isBurnable();
    }

    public boolean hasCollision(ScriptWorld world, int x, int y, int z) {
        return this.blockState.getCollisionShape(world.getMinecraftWorld(), new BlockPos(x, y, z)) != null;
    }

    public List<String> getProperties() {
        List<String> properties = new ArrayList<>();

        this.blockState.getProperties().forEach((p) -> {
            properties.add(p.getName());
        });

        return properties;
    }

    public String getName() {
        return this.blockState.getBlock().getName().toString();
    }

    public float getBlastResistance() {
        return this.blockState.getBlock().getBlastResistance();
    }

    public float getSlipperiness() {
        return this.blockState.getBlock().getSlipperiness();
    }

    public float getHardness() {
        return this.blockState.getBlock().getHardness();
    }

    public float getVelocityMultiplier() {
        return this.blockState.getBlock().getVelocityMultiplier();
    }

    public float getJumpVelocityMultiplier() {
        return this.blockState.getBlock().getJumpVelocityMultiplier();
    }

    public Object getProperty(String name) throws Exception {
        return this.blockState.getProperties().stream()
                .filter(property -> property.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(property -> this.blockState.get(property))
                .orElseThrow(() -> new CubeCodeException("Unknown property: " + name));
    }

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

package com.cubecode.api.scripts.code.blocks;

import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

/**
 * ScriptBlockEntity represents a block entity (tile entity) in the Minecraft world.
 * Block entities are special blocks that can store additional data and perform updates, such as chests, furnaces, or custom modded blocks.
 *
 * Block entities maintain their own state and can interact with the world around them.
 * They are positioned at specific coordinates and can store custom data using NBT compounds.
 */
public class ScriptBlockEntity {
    private final BlockEntity blockEntity;

    public ScriptBlockEntity(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    /**
     * Returns the underlying Minecraft BlockEntity instance.
     *
     * This method provides access to the native Minecraft block entity object,
     * which can be useful for advanced operations or compatibility with other mods.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(x, y, z);
     * const minecraftBlockEntity = blockEntity.getMinecraftTileEntity();
     * }</pre>
     */
    public BlockEntity getMinecraftTileEntity() {
        return this.blockEntity;
    }

    /**
     * Returns the ScriptWorld instance containing this block entity.
     *
     * Use this method to access the world for performing operations in the
     * context of this block entity's environment.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(2, 2, 2);
     * const entityWorld = blockEntity.getWorld();
     * const blockAbove = entityWorld.getBlock(2, 2 + 1, 2);
     * }</pre>
     */
    public ScriptWorld getWorld() {
        return new ScriptWorld(this.blockEntity.getWorld());
    }

    /**
     * Returns the identifier type of this block entity.
     *
     * The type is returned as a string in the format "namespace:name",
     * e.g., "minecraft:chest" or "minecraft:furnace"
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(1, 1, 1);
     * const entityType = blockEntity.getType();
     * if (entityType === "minecraft:chest") {
     *     // Handle chest-specific logic
     * }
     * }</pre>
     */
    public String getType() {
        return this.blockEntity.getType().toString();
    }

    /**
     * Returns the position of this block entity in the world.
     *
     * The position is returned as a ScriptVector containing the x, y, and z coordinates.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(7, 7, 7);
     * const pos = blockEntity.getPosition();
     * c.server.send(`Block entity is at: ${pos.x}, ${pos.y}, ${pos.z}`, false);
     * }</pre>
     */
    public ScriptVector getPosition() {
        return new ScriptVector(this.blockEntity.getPos());

    }

    /**
     * Marks this block entity as dirty, forcing a data save and sync.
     *
     * Call this method after modifying the block entity's data to ensure
     * changes are properly saved and synchronized with clients.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(6, 6, 6);
     * const data = blockEntity.getNbtCompound();
     * data.putString("customData", "newValue");
     * blockEntity.setNbtCompound(data);
     * blockEntity.markDirty(); // Ensure changes are saved
     * }</pre>
     */
    public void markDirty() {
        this.blockEntity.markDirty();
    }

    /**
     * Returns the ScriptBlockState of the block associated with this block entity.
     *
     * Use this to access or modify the block's properties and state.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(4, 4, 4);
     * const blockState = blockEntity.getBlock();
     * c.server.send(`Block Id: ${blockState.getId()}`, false);
     * }</pre>
     */
    public ScriptBlockState getBlock() {
        return ScriptBlockState.create(this.blockEntity.getCachedState());
    }

    /**
     * Returns the NBT compound containing this block entity's data.
     *
     * The NBT compound contains all custom data stored in the block entity.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(3, 3, 3);
     * const nbt = blockEntity.getNbtCompound();
     * if (nbt.contains("CustomName")) {
     *     const name = nbt.getString("CustomName");
     *     c.server.send(`Block entity name: ${name}`, false);
     * }
     * }</pre>
     */
    public ScriptNbtCompound getNbtCompound() {
        return new ScriptNbtCompound(this.blockEntity.createNbt());
    }

    /**
     * Sets the NBT compound data for this block entity.
     *
     * Use this method to update the block entity's stored data.
     * Remember to call markDirty() after setting new data.
     *
     * <pre>{@code
     * const blockEntity = c.world.getBlockEntity(2, 2, 2);
     * const nbt = blockEntity.getNbtCompound();
     * nbt.putInt("StoredValue", 42);
     * blockEntity.setNbtCompound(nbt);
     * blockEntity.markDirty();
     * }</pre>
     */
    public void setNbtCompound(ScriptNbtCompound nbtCompound) {
        this.blockEntity.readNbt(nbtCompound.getMinecraftNbtCompound());
        this.blockEntity.markDirty();
    }
}

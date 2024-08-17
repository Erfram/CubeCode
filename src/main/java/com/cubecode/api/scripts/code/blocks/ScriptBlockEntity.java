package com.cubecode.api.scripts.code.blocks;

import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public class ScriptBlockEntity {
    private BlockEntity blockEntity;

    public ScriptBlockEntity(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public BlockEntity getMinecraftTileEntity()
    {
        return this.blockEntity;
    }

    public ScriptWorld getWorld() {
        return new ScriptWorld(this.blockEntity.getWorld());
    }

    public String getType() {
        return this.blockEntity.getType().toString();
    }

    public ScriptVector getPosition() {
        return new ScriptVector(this.blockEntity.getPos());

    }

    public void markDirty() {
        this.blockEntity.markDirty();
    }

    public ScriptBlockState getBlock() {
        return ScriptBlockState.create(this.blockEntity.getCachedState());
    }

    public ScriptNbtCompound getNbtCompound() {
        return new ScriptNbtCompound(this.blockEntity.createNbt());
    }

    public void setNbtCompound(ScriptNbtCompound nbtCompound) {
        this.blockEntity.readNbt(nbtCompound.getMinecraftNbtCompound());
        this.blockEntity.markDirty();
    }
}

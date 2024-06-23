package com.cubecode.api.scripts.code;


import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import com.cubecode.api.scripts.code.entities.ScriptEntity;

public class ScriptRayTrace {
    HitResult result;
    ScriptEntity<?> entity;

    public ScriptRayTrace(HitResult hitResult) {
        this.result = hitResult;
    }

    public boolean isBlock() {
        return this.result.getType() == HitResult.Type.BLOCK;
    }

    public boolean isEntity() {
        return this.result.getType() == HitResult.Type.ENTITY;
    }

    public boolean isMissed() {
        return this.result.getType() == HitResult.Type.MISS;
    }

    public ScriptEntity<?> getEntity() {
        if(this.result == null || !this.isEntity()) {
            return null;
        }

        if (this.entity == null) {
            this.entity = ScriptEntity.create(((EntityHitResult) this.result).getEntity());
        }

        return this.entity;
    }

    public ScriptVector getBlock() {
        if(this.isBlock()) {
            return new ScriptVector(((BlockHitResult)this.result).getBlockPos());
        }

        return null;
    }

    public String getBlockSide() {
        if(this.isBlock()) {
            return ((BlockHitResult)this.result).getSide().getName();
        }

        return null;
    }

    public ScriptVector getHitPosition() {
        return new ScriptVector(this.result.getPos());
    }
}

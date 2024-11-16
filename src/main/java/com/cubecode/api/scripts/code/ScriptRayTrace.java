package com.cubecode.api.scripts.code;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import com.cubecode.api.scripts.code.entities.ScriptEntity;

/**
 * Docs for rayTrace bruh
 *
 * <pre>{@code
 * var rayTrace = c.getPlayer().rayTraceBlock(5, false);
 *
 * c.getPlayer().send(rayTrace.getBlock().getBlockId();
 * }</pre>
 */
public class ScriptRayTrace {
    HitResult result;
    ScriptEntity<?> entity;

    public ScriptRayTrace(HitResult hitResult) {
        this.result = hitResult;
    }

    /**
     * Checks if the ray trace hit a block
     *
     * <pre>{@code
     * c.getPlayer().rayTraceBlock(5, false).isBlock();
     * }</pre>
     */
    public boolean isBlock() {
        return this.result.getType() == HitResult.Type.BLOCK;
    }

    /**
     * Checks if the ray trace hit an entity
     */
    public boolean isEntity() {
        return this.result.getType() == HitResult.Type.ENTITY;
    }

    /**
     * Checks if the ray trace missed and hit nothing
     */
    public boolean isMissed() {
        return this.result.getType() == HitResult.Type.MISS;
    }

    /**
     * Returns the entity hit by the ray trace, if any
     */
    public ScriptEntity<?> getEntity() {
        if(this.result == null || !this.isEntity()) {
            return null;
        }

        if (this.entity == null) {
            this.entity = ScriptEntity.create(((EntityHitResult) this.result).getEntity());
        }

        return this.entity;
    }

    /**
     * Returns the position of the block hit by the ray trace, if any
     */
    public ScriptVector getBlock() {
        if(this.isBlock()) {
            return new ScriptVector(((BlockHitResult)this.result).getBlockPos());
        }

        return null;
    }

    /**
     * Returns the side of the block that was hit by the ray trace
     */
    public String getBlockSide() {
        if(this.isBlock()) {
            return ((BlockHitResult)this.result).getSide().getName();
        }

        return null;
    }

    /**
     * Returns the exact position where the ray trace hit
     */
    public ScriptVector getHitPosition() {
        return new ScriptVector(this.result.getPos());
    }
}

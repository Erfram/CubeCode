package com.cubecode.api.scripts.code;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ScriptVector {
    public double x;
    public double y;
    public double z;

    public ScriptVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ScriptVector(Vec3d vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public ScriptVector(BlockPos blockPos) {
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    public ScriptVector add(ScriptVector other) {
        return new ScriptVector(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    public ScriptVector subtract(ScriptVector other) {
        return new ScriptVector(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public ScriptVector multiply(double scalar) {
        return new ScriptVector(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public ScriptVector normalize() {
        double length = this.length();
        return new ScriptVector(
                this.x / length,
                this.y / length,
                this.z / length);
    }

    public BlockPos toBlockPos() {
        return new BlockPos((int) this.x, (int) this.y, (int) this.z);
    }

    public Vec3d toVec3d() {
        return new Vec3d(this.x, this.y, this.z);
    }

    public String toString() {
        return "("+x+", "+y+", "+z+")";
    }

    public String toArrayString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}
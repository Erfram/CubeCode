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

    /**
     * Adds another vector to this vector
     *
     * <pre>{@code
     * CubeCode.vector(434, 34, 443).add(CubeCode.vector(22, 232, 232));
     * }</pre>
     */
    public ScriptVector add(ScriptVector other) {
        return new ScriptVector(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    /**
     * Subtracts another vector from this vector
     *
     * <pre>{@code
     * CubeCode.vector(434, 34, 443).subtract(CubeCode.vector(22, 232, 232));
     * }</pre>
     */
    public ScriptVector subtract(ScriptVector other) {
        return new ScriptVector(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    /**
     * Multiplies this vector by a scalar value
     *
     * <pre>{@code
     * CubeCode.vector(434, 34, 443).multiply(2);
     * }</pre>
     */
    public ScriptVector multiply(double scalar) {
        return new ScriptVector(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    public ScriptVector cross(ScriptVector vector) {
        return new ScriptVector(
                y * vector.z - z * vector.y,
                z * vector.x - x * vector.z,
                x * vector.y - y * vector.x
        );
    }

    /**
     * Calculates the length of this vector
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Returns a normalized version of this vector
     */
    public ScriptVector normalize() {
        double length = this.length();
        return new ScriptVector(
                this.x / length,
                this.y / length,
                this.z / length);
    }

    /**
     * Converts this vector to a BlockPos object
     */
    public BlockPos toBlockPos() {
        return new BlockPos((int) this.x, (int) this.y, (int) this.z);
    }

    /**
     * Converts this vector to a Vec3d object
     */
    public Vec3d toVec3d() {
        return new Vec3d(this.x, this.y, this.z);
    }

    /**
     * Returns a string representation of this vector
     */
    public String toString() {
        return "("+x+", "+y+", "+z+")";
    }

    /**
     * Returns a string representation of this vector as an array
     */
    public String toArrayString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}

package com.cubecode.api.project.scripts.code;

public class ScriptFactory {
    /**
     * Returns the class name of the given object
     *
     * <pre>{@code
     * CubeCode.getClassName(c.getPlayer());
     * }</pre>
     */
    public String getClassName(Object value) {
        String classes = value.getClass().toString();
        int beginIndex = classes.lastIndexOf(".") + 1;

        return classes.substring(beginIndex);
    }

    /**
     * Creates a new ScriptVector with the given x, y, and z coordinates
     *
     * <pre>{@code
     * CubeCode.vector(232, 232, 223);
     * }</pre>
     */
    public ScriptVector vector(double x, double y, double z) {
        return new ScriptVector(x, y, z);
    }
}

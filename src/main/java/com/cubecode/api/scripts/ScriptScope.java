package com.cubecode.api.scripts;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;

public class ScriptScope extends ScriptableObject implements Scriptable {
    Context cx;
    String name;
    public ScriptScope(String name, Context cx) {
        super();
        this.cx = cx;
        this.name = name;
    }

    public Object get(String key) {
        return this.get(cx, key);
    }

    public void set(String key, Object value) {
        this.put(cx, key, this, value);
    }

    @Override
    public String getClassName() {
        return "ScriptScope";
    }

    public String toString() {
        return this.name;
    }
}
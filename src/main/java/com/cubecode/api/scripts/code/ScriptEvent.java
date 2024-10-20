package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.api.scripts.code.entities.ScriptPlayer;

import java.util.HashMap;
import java.util.Map;

public class ScriptEvent {
    private final String script;
    private final String function;

    private final ScriptEntity subject;
    private final ScriptEntity object;
    private final ScriptWorld world;
    private final ScriptServer server;

    private Map<String, Object> values = new HashMap<>();
    private boolean canceled = false;

    public ScriptEvent(String script, String function, ScriptEntity subject, ScriptEntity object, ScriptWorld world, ScriptServer server) {
        this.script = script;
        this.function = function;

        this.subject = subject;
        this.object = object;
        this.world = world;
        this.server = server;
    }

    public String getScript() {
        return this.script;
    }

    public String getFunction() {
        return this.function;
    }

    public ScriptEntity getSubject() {
        return this.subject;
    }

    public ScriptEntity getObject() {
        return this.object;
    }

    public ScriptWorld getWorld() {
        return this.world;
    }

    public ScriptServer getServer() {
        return this.server;
    }

    public ScriptPlayer getPlayer() {
        ScriptEntity subject = this.getSubject();
        ScriptEntity object = this.getObject();

        if (subject instanceof ScriptPlayer) {
            return (ScriptPlayer) subject;
        } else if (object instanceof ScriptPlayer) {
            return (ScriptPlayer) object;
        }

        return null;
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public Object getValue(String key) {
        return this.values.get(key);
    }

    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public void cancel() {
        this.canceled = true;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return this.canceled;
    }
}
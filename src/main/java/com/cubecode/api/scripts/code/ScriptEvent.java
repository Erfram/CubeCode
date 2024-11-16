package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.api.scripts.code.entities.ScriptPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Docs for event bruh
 *
 * <pre>{@code
 * c.getPlayer().send(c.getPlayer().getName());
 * }</pre>
 */
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

    /**
     * Returns the function associated with this event
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * Returns the subject entity of this event
     */
    public ScriptEntity getSubject() {
        return this.subject;
    }

    /**
     * Returns the object entity of this event
     */
    public ScriptEntity getObject() {
        return this.object;
    }

    /**
     * Returns the world in which this event occurred
     */
    public ScriptWorld getWorld() {
        return this.world;
    }

    /**
     * Returns the server associated with this event
     */
    public ScriptServer getServer() {
        return this.server;
    }

    /**
     * Returns the player associated with this event
     */
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

    /**
     * Returns a map of all values associated with this event
     */
    public Map<String, Object> getValues() {
        return this.values;
    }

    /**
     * Returns the value associated with the specified key
     */
    public Object getValue(String key) {
        return this.values.get(key);
    }

    /**
     * Sets a value for the specified key
     *
     * <pre>{@code
     * c.setValue("id", "dyamo");
     * }</pre>
     */
    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    /**
     * Cancels this event
     */
    public void cancel() {
        this.canceled = true;
    }

    /**
     * Sets the canceled state of this event
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Checks if this event is canceled
     */
    public boolean isCanceled() {
        return this.canceled;
    }
}
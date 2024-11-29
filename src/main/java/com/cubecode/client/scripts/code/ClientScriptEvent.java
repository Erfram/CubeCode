package com.cubecode.client.scripts.code;

import com.cubecode.client.scripts.code.entities.ClientScriptEntity;
import com.cubecode.client.scripts.code.entities.ClientScriptPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * ClientScriptEvent represents an event in the client-side scripting system of a Minecraft mod.
 * It encapsulates details about the event, including the script being executed, the function being called,
 * the entities involved in the event (subject and object), and the world context in which the event occurs.
 *
 * This class allows for the storage and retrieval of event-specific values, provides mechanisms to cancel
 * the event, and helps in identifying player entities involved in the event.
 */
public class ClientScriptEvent {
    private final String script;
    private final String function;

    private final ClientScriptEntity subject;
    private final ClientScriptEntity object;
    private final ClientScriptWorld world;

    private Map<String, Object> values = new HashMap<>();
    private boolean canceled = false;

    public ClientScriptEvent(String script, String function, ClientScriptEntity subject, ClientScriptEntity object, ClientScriptWorld world) {
        this.script = script;
        this.function = function;

        this.subject = subject;
        this.object = object;
        this.world = world;
    }

    /**
     * Retrieves the name of the script associated with this event.
     */
    public String getScript() {
        return this.script;
    }

    /**
     * Retrieves the name of the function being executed within the script.
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * Retrieves the subject entity involved in the event.
     */
    public ClientScriptEntity getSubject() {
        return this.subject;
    }

    /**
     * Retrieves the object entity involved in the event.
     */
    public ClientScriptEntity getObject() {
        return this.object;
    }

    /**
     * Retrieves the world context in which the event occurs.
     */
    public ClientScriptWorld getWorld() {
        return this.world;
    }

    /**
     * Retrieves the player entity involved in the event, if any.
     */
    public ClientScriptPlayer getPlayer() {
        ClientScriptEntity subject = this.getSubject();
        ClientScriptEntity object = this.getObject();

        if (subject instanceof ClientScriptPlayer) {
            return (ClientScriptPlayer) subject;
        } else if (object instanceof ClientScriptPlayer) {
            return (ClientScriptPlayer) object;
        }

        return null;
    }

    /**
     * Retrieves the map of values associated with this event.
     */
    public Map<String, Object> getValues() {
        return this.values;
    }

    /**
     * Retrieves a specific value associated with the given key.
     */
    public Object getValue(String key) {
        return this.values.get(key);
    }

    /**
     * Sets a specific value associated with the given key.
     */
    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    /**
     * Sets multiple values associated with this event using a map.
     */
    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    /**
     * Cancels the event, preventing further processing.
     */
    public void cancel() {
        this.canceled = true;
    }

    /**
     * Sets the canceled state of the event.
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Checks if the event has been canceled.
     */
    public boolean isCanceled() {
        return this.canceled;
    }
}

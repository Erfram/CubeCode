package com.cubecode.client.scripts.code;

import com.cubecode.client.scripts.code.entities.ClientScriptEntity;
import com.cubecode.client.scripts.code.entities.ClientScriptPlayer;

import java.util.HashMap;
import java.util.Map;

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

    public String getScript() {
        return this.script;
    }

    public String getFunction() {
        return this.function;
    }

    public ClientScriptEntity getSubject() {
        return this.subject;
    }

    public ClientScriptEntity getObject() {
        return this.object;
    }

    public ClientScriptWorld getWorld() {
        return this.world;
    }

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

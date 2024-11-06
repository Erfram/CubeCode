package com.cubecode.client.scripts;

import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.api.scripts.code.ScriptServer;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.client.scripts.code.ClientScriptEvent;
import com.cubecode.client.scripts.code.ClientScriptWorld;
import com.cubecode.client.scripts.code.entities.ClientScriptEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class ClientProperties {
    private final Map<String, Object> map = new HashMap<>();

    public static ClientProperties create() {
        return new ClientProperties();
    }

    public static ClientProperties create(String script, String function, Entity subject, Entity object, ClientWorld world) {
        ClientProperties properties = new ClientProperties();

        properties.map.put("Context", new ClientScriptEvent(
                script,
                function,
                ClientScriptEntity.create(subject),
                ClientScriptEntity.create(object),
                world == null ? null : new ClientScriptWorld(world)
        ));

        return properties;
    }

    public Map<String, Object> getMap() {
        return this.map;
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public void put(String key, Object value) {
        this.map.put(key, value);
    }

    public ClientProperties setValue(String key, Object value) {
        ScriptEvent scriptEvent = (ScriptEvent) this.map.get("Context");

        scriptEvent.setValue(key, value);

        this.map.put("Context", scriptEvent);

        return this;
    }

    public ClientProperties copy() {
        ClientScriptEvent scriptEvent = (ClientScriptEvent) this.map.get("Context");

        return ClientProperties.create(
                scriptEvent.getScript(),
                scriptEvent.getFunction(),
                scriptEvent.getSubject() == null ? null : scriptEvent.getSubject().getMinecraftEntity(),
                scriptEvent.getObject() == null ? null : scriptEvent.getObject().getMinecraftEntity(),
                scriptEvent.getWorld() == null ? null : scriptEvent.getWorld().getMinecraftWorld()
        );
    }
}

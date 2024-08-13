package com.cubecode.api.scripts;

import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.api.scripts.code.ScriptServer;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.client.config.CubeCodeConfig;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class Properties {
    private final Map<String, Object> map = new HashMap<>();

    public static Properties create() {
        return new Properties();
    }

    public static Properties create(String script, String function, Entity subject, Entity object, World world, MinecraftServer server) {
        Properties properties = new Properties();

        properties.map.put(CubeCodeConfig.getScriptConfig().contextName, new ScriptEvent(
            script,
            function,
            ScriptEntity.create(subject),
            ScriptEntity.create(object),
            world == null ? null : new ScriptWorld(world),
            server == null ? null : new ScriptServer(server)
        ));

        properties.put("CubeCode", new ScriptFactory());

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
}

package com.cubecode.api.scripts;

import com.cubecode.client.scripts.code.ClientScriptEvent;
import com.cubecode.client.scripts.code.ClientScriptWorld;
import com.cubecode.client.scripts.code.entities.ClientScriptEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class ClientProperties extends Properties {

    public static ClientProperties create(String script, String function, Entity subject, Entity object, ClientWorld world) {
        ClientProperties properties = new ClientProperties();

        properties.getMap().put("Context", new ClientScriptEvent(
                script,
                function,
                ClientScriptEntity.create(subject),
                ClientScriptEntity.create(object),
                world == null ? null : new ClientScriptWorld(world)
        ));

        return properties;
    }
}

package com.cubecode.client.scripts.code.entities;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class ClientScriptEntity<T extends Entity> {
    protected T entity;

    public static ClientScriptEntity create(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return new ClientScriptPlayer((ClientPlayerEntity) entity);
        } else if (entity != null) {
            return new ClientScriptEntity<Entity>(entity);
        }

        return null;
    }

    protected ClientScriptEntity(T entity) {
        this.entity = entity;
    }

    /**
     * Returns the underlying Minecraft entity object.
     * This method is primarily for internal use and advanced scripting.
     */
    public Entity getMinecraftEntity() {
        return this.entity;
    }
}

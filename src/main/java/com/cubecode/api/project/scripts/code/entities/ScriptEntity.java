package com.cubecode.api.project.scripts.code.entities;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;


public class ScriptEntity<T extends Entity> {
    protected T entity;

    public static ScriptEntity<?> create(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return new ScriptPlayer((ServerPlayerEntity) entity);
        } else if (entity != null) {
            return new ScriptEntity<Entity>(entity);
        }

        return null;
    }

    protected ScriptEntity(T entity) {
        this.entity = entity;
    }

    public T getMinecraftEntity() {
        return entity;
    }

    public void addVelocity(double x, double y, double z) {
        this.entity.setVelocity(this.entity.getVelocity().add(x, y, z));
    }


    public void setVelocity(double x, double y, double z) {
        this.entity.kill();
    }

}






































































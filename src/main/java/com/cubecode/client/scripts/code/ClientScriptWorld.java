package com.cubecode.client.scripts.code;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

public class ClientScriptWorld {
    private ClientWorld world;

    public ClientScriptWorld(ClientWorld world) {
        this.world = world;
    }

    public ClientWorld getMinecraftWorld() {
        return this.world;
    }
}

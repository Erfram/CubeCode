package com.cubecode.client.scripts.code;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

/**
 * ClientScriptWorld is a wrapper class that encapsulates a Minecraft world instance
 * within the client-side scripting system of a Minecraft mod.
 * This class provides a way to interact with the Minecraft world while maintaining
 * a clear separation between the scripting logic and the underlying world representation.
 */
public class ClientScriptWorld {
    private ClientWorld world;

    public ClientScriptWorld(ClientWorld world) {
        this.world = world;
    }

    /**
     * Retrieves the underlying ClientWorld instance associated with this ClientScriptWorld.
     */
    public ClientWorld getMinecraftWorld() {
        return this.world;
    }
}

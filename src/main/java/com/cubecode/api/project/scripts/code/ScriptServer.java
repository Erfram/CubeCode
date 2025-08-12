package com.cubecode.api.project.scripts.code;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

/**
 * Tab with methods that can be used on server.
 */
public class ScriptServer {
    private MinecraftServer server;

    public ScriptServer(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getMinecraftServer() {
        return this.server;
    }

    public void send(String message) {
        this.server.getPlayerManager().broadcast(Text.of(message), false);
    }



}









































package com.cubecode.api.scripts.code;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.util.ArrayList;

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

    public int getMaxPlayerCount() {
        return this.server.getMaxPlayerCount();
    }

    public boolean isSingleplayer() {
        return this.server.isSingleplayer();
    }

    public boolean isDedicated() {
        return this.server.isDedicated();
    }

    public boolean isHardcore() {
        return this.server.isHardcore();
    }


    public boolean isOnlineMode() {
        return this.server.isOnlineMode();
    }

    public int getTicks() {
        return this.server.getTicks();
    }

    public void send(String message, boolean overlay) {
        this.server.getPlayerManager().broadcast(Text.of(message), overlay);
    }
}

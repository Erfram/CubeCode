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

    /**
     *
     */
    public MinecraftServer getMinecraftServer() {
        return this.server;
    }

    /**
     * Returns the maximum number of players allowed on the server
     */
    public int getMaxPlayerCount() {
        return this.server.getMaxPlayerCount();
    }

    /**
     * Checks if the server is running in singleplayer mode
     */
    public boolean isSingleplayer() {
        return this.server.isSingleplayer();
    }

    /**
     * Checks if the server is a dedicated server
     */
    public boolean isDedicated() {
        return this.server.isDedicated();
    }

    /**
     * Checks if the server is running in hardcore mode
     */
    public boolean isHardcore() {
        return this.server.isHardcore();
    }

    /**
     * Checks if the server is running in online mode
     */
    public boolean isOnlineMode() {
        return this.server.isOnlineMode();
    }

    /**
     * Returns the current number of ticks of the server
     */
    public int getTicks() {
        return this.server.getTicks();
    }

    /**
     * Sends a message to all players on the server
     *
     * <pre>{@code
     * c.getServer().send("Beta", false);
     * }</pre>
     */
    public void send(String message, boolean overlay) {
        this.server.getPlayerManager().broadcast(Text.of(message), overlay);
    }
}

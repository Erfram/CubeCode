package com.cubecode.api.scripts.code;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;

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



    public String[] getPlayerNames() {
        return this.server.getPlayerNames();
    }

    public boolean isWhitelistEnabled() {
        return this.server.isEnforceWhitelist();
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

    public boolean isNetherEnabled() {
        return this.server.isNetherAllowed();
    }

    public boolean isPvpEnabled() {
        return this.server.isPvpEnabled();
    }

    public boolean isMonsterSpawningEnabled() {
        return this.server.isMonsterSpawningEnabled();
    }

    public boolean isFlightEnabled() {
        return this.server.isFlightEnabled();
    }

    public int getPlayerIdleTimeout() {
        return this.server.getPlayerIdleTimeout();
    }

    public int getTicks() {
        return this.server.getTicks();
    }

    public int getMaxPlayerCount() {
        return this.server.getMaxPlayerCount();
    }

    public int getPlayerCount() {
        return this.server.getCurrentPlayerCount();
    }

    public void send(String message, boolean overlay) {
        this.server.getPlayerManager().broadcast(Text.of(message), overlay);
    }

    public void setDifficultyLocked(boolean locked) {
        this.server.setDifficultyLocked(locked);
    }

    public void setDifficulty(int difficulty) {
        this.server.setDifficulty(Difficulty.byId(difficulty), false);
    }

    public void setDifficulty(String difficulty) {
        this.server.setDifficulty(Difficulty.byName(difficulty), false);
    }

    public void setDefaultGameMode(int gamemode) {
        this.server.setDefaultGameMode(GameMode.byId(gamemode));
    }

    public void setDefaultGameMode(String gamemode) {
        this.server.setDefaultGameMode(GameMode.byName(gamemode));
    }

    public void setWhitelist(boolean isEnabled) {
        this.server.setEnforceWhitelist(isEnabled);
    }

    public void setFlightEnabled(boolean isEnabled) {
        this.server.setFlightEnabled(isEnabled);
    }

    public void setPvpEnabled(boolean isEnabled) {
        this.server.setPvpEnabled(isEnabled);
    }

    public void setPlayerIdleTimeout(int timeout) {
        this.server.setPlayerIdleTimeout(timeout);
    }

}

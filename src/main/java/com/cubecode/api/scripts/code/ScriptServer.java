package com.cubecode.api.scripts.code;

import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import com.cubecode.CubeCode;
import com.cubecode.api.scripts.code.cubecode.CubeCodeStates;
import com.cubecode.api.scripts.code.entities.ScriptPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptServer {
    private MinecraftServer server;

    public ScriptServer(MinecraftServer server) {
        this.server = server;
    }

    public List<ScriptPlayer> getAllPlayers() {
        List<ScriptPlayer> players = new ArrayList<>();
        this.server.getPlayerManager().getPlayerList().forEach((player) -> players.add((ScriptPlayer) ScriptPlayer.create(player)));

        return players;
    }

    /**
     *
     * overworld or nether or end
     *
     */
    public ScriptWorld getWorld(String worldName) {
        return new ScriptWorld(this.server.getWorld(worldName.equals("overworld") ? World.OVERWORLD : worldName.equals("nether") ? World.NETHER : World.END));
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

    public ScriptPlayer getPlayer(String nickName) {
        return (ScriptPlayer) ScriptPlayer.create(this.server.getPlayerManager().getPlayer(nickName));
    }

    public CubeCodeStates getStates() {
        return new CubeCodeStates(this.server);
    }

    public void executeScript(String scriptName) throws Exception {
        Map<String, Object> properties = new HashMap<>();

        properties.put("Server", this);

        CubeCode.scriptManager.executeScript(scriptName, properties);
    }
}
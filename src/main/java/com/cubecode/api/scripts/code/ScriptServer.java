package com.cubecode.api.scripts.code;

import com.cubecode.utils.CubeCodeException;
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

/**
 * Tab with methods that can be used on server.
 */
public class ScriptServer {
    private MinecraftServer server;

    public ScriptServer(MinecraftServer server) {
        this.server = server;
    }

    /**
     * Returns a list of all players on the server
     *
     * <pre>{@code
     * var allPlayers = c.getServer().getAllPlayers();
     *
     * c.getPlayer().send(allPlayers[Math.floor(Math.random() * allPlayers.length)].getName());
     * }</pre>
     */
    public List<ScriptPlayer> getAllPlayers() {
        List<ScriptPlayer> players = new ArrayList<>();
        this.server.getPlayerManager().getPlayerList().forEach((player) -> players.add((ScriptPlayer) ScriptPlayer.create(player)));

        return players;
    }

    /**
     * Returns the world with the specified name.
     */
    public ScriptWorld getWorld(String worldName) {
        return new ScriptWorld(this.server.getWorld(worldName.equals("overworld") ? World.OVERWORLD : worldName.equals("nether") ? World.NETHER : World.END));
    }

    /**
     * Returns the world
     */
    public ScriptWorld getWorld() {
        return new ScriptWorld(this.server.getWorld(World.OVERWORLD));
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

    /**
     * Returns the player with the specified nickname
     *
     * <pre>{@code
     * c.getServer().getPlayer("Llama");
     * }</pre>
     */
    public ScriptPlayer getPlayer(String nickName) {
        return (ScriptPlayer) ScriptPlayer.create(this.server.getPlayerManager().getPlayer(nickName));
    }

    /**
     * Returns the CubeCode states for the server
     */
    public CubeCodeStates getStates() {
        return new CubeCodeStates(this.server);
    }

    /**
     * Executes a script with the specified name
     *
     * c.getServer().executeScript("docs.js");
     *
     * //docs.js
     * c.server.send("CubeCode", false);
     */
    public void executeScript(String scriptName) throws CubeCodeException {
        Map<String, Object> properties = new HashMap<>();

        properties.put("Context", new ScriptEvent(
                scriptName,
                null,
                null,
                null,
                null,
                this
        ));

        CubeCode.projectManager.evalCode(CubeCode.projectManager.getScript(scriptName).getCode(), scriptName, properties);
    }
}
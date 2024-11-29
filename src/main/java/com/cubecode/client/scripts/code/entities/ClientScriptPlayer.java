package com.cubecode.client.scripts.code.entities;

import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.api.scripts.code.nbt.ScriptNbtCompound;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.screens.TestScreen;
import com.cubecode.client.scripts.code.ui.ClientCubeCodeUI;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.RunScriptPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientScriptPlayer extends ClientScriptEntity<ClientPlayerEntity> {
    public ClientScriptPlayer(ClientPlayerEntity player) {
        super(player);
    }

    /**
     * Retrieves the Minecraft player entity associated with this instance.
     */
    public Entity getMinecraftPlayer() {
        return this.entity;
    }

    /**
     * Retrieves the current instance of the Minecraft client.
     */
    public MinecraftClient getMinecraftClient() {
        return MinecraftClient.getInstance();
    }


    /**
     * Sends a chat message to the player.
     *
     * <pre>{@code
     * player.send("Hello world!");
     * player.send("§cColored text");
     * }</pre>
     */
    public void send(String message) {
        this.entity.sendMessage(Text.of(message));
    }

    /**
     * Gets the player's current rotation angles.
     * Returns a vector where:
     * x = pitch (vertical rotation, -90 to 90)
     * y = yaw (horizontal rotation, -180 to 180)
     * z = head yaw (head rotation, -180 to 180)
     *
     * <pre>{@code
     * var rotations = player.getRotations();
     * c.player.send("Pitch: " + rotations.x);
     * c.player.send("Yaw: " + rotations.y);
     * c.player.send("Head Yaw: " + rotations.z);
     * }</pre>
     */
    public ScriptVector getRotations() {
        return new ScriptVector(this.entity.getPitch(), this.entity.getYaw(), this.entity.getHeadYaw());
    }

    /**
     * Sets the player's rotation angles.
     *
     * <pre>{@code
     * // Look straight up
     * player.setRotations(-90, 0, 0);
     *
     * // Look straight down
     * player.setRotations(90, 0, 0);
     * }</pre>
     */
    public void setRotations(float pitch, float yaw, float headYaw) {
        this.entity.setPitch(pitch);
        this.entity.setYaw(yaw);
        this.entity.setHeadYaw(headYaw);
    }

    /**
     * Sets the player's rotation angles including body rotation.
     *
     * <pre>{@code
     * // Look and turn body to the right
     * player.setRotations(0, 90, 90, 90);
     * }</pre>
     */
    public void setRotations(float pitch, float yaw, float headYaw, float bodyYaw) {
        this.entity.setPitch(pitch);
        this.entity.setYaw(yaw);
        this.entity.setHeadYaw(headYaw);
        this.entity.setBodyYaw(bodyYaw);
    }

    /**
     * Plays a sound for the player at their location.
     *
     * The sound identifier (e.g. "block.note_block.harp")
     * The sound category (e.g. "master", "music", "records", "weather", "blocks", "hostile", "neutral", "players", "ambient", "voice")
     * The sound volume (0.0 to 1.0)
     * The sound pitch (0.5 to 2.0)
     *
     * <pre>{@code
     * // Play a creeper hiss
     * player.playSound("entity.creeper.primed", "hostile", 1.0, 1.0);
     *
     * // Play a note block sound
     * player.playSound("block.note_block.harp", "records", 1.0, 1.0);
     * }</pre>
     */
    public void playSound(String sound, String soundCategory, float volume, float pitch) {
        this.entity.playSound(SoundEvent.of(new Identifier(sound)), SoundCategory.valueOf(soundCategory.toUpperCase()), volume, pitch);
    }

    /**
     * Gets the player's game window resolution.
     * Returns a vector where:
     * X = window width in pixels
     * Y = window height in pixels
     * Z = always 0
     *
     * <pre>{@code
     * var res = player.getResolution();
     * c.player.send("Window size: " + res.x + "x" + res.y);
     * }</pre>
     */
    public ScriptVector getResolution() {
        return new ScriptVector(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight(), 0);
    }

    /**
     * Opens a custom UI screen for the player.
     * The UI will pause the game and have a dark background.
     *
     * <pre>{@code
     * var ui = CubeCode.createUI();
     *
     * ui.window("window", () => {
     *     var button = ui.button("click me")
     *         .onClick(() => {
     *             button.setLabel("Clicked!")
     *         })
     *         .rxy(0.5, 0.5)
     *         .rwh(0.5, 0.5)
     * }).rxy(0.5, 0.5).rwh(0.5, 0.5).render()
     *
     * c.player.openUI(ui);
     * }</pre>
     */
    public void openUI(ClientCubeCodeUI clientCubeCodeUI) {
        MinecraftClient.getInstance().setScreen(new TestScreen(true, true, clientCubeCodeUI.getView()));
    }

    /**
     * Opens a custom UI screen for the player with background visibility control.
     * The UI will pause the game.
     *
     * <pre>{@code
     * var ui = CubeCode.createUI();
     * ui.window("window", () => {
     *     var button = ui.button("click me")
     *         .onClick(() => {
     *             button.setLabel("Clicked!")
     *         })
     *         .rxy(0.5, 0.5)
     *         .rwh(0.5, 0.5)
     * }).rxy(0.5, 0.5).rwh(0.5, 0.5).render()
     * player.openUI(ui, false); // transparent background
     * }</pre>
     */
    public void openUI(ClientCubeCodeUI clientCubeCodeUI, boolean isBackground) {
        MinecraftClient.getInstance().setScreen(new TestScreen(isBackground, true, clientCubeCodeUI.getView()));
    }

    /**
     * Opens a custom UI screen for the player with full control over background and pause state.
     *
     * <pre>{@code
     * var ui = CubeCode.createUI();
     * ui.window("window", () => {
     *     var button = ui.button("click me")
     *         .onClick(() => {
     *             button.setLabel("Clicked!")
     *         })
     *         .rxy(0.5, 0.5)
     *         .rwh(0.5, 0.5)
     * }).rxy(0.5, 0.5).rwh(0.5, 0.5).render()
     * player.openUI(ui, false, false); // transparent background, doesn't pause game
     * }</pre>
     */
    public void openUI(ClientCubeCodeUI clientCubeCodeUI, boolean isBackground, boolean shouldPause) {
        MinecraftClient.getInstance().setScreen(new TestScreen(isBackground, shouldPause, clientCubeCodeUI.getView()));
    }

    /**
     * Closes any open UI screens and returns to the game view.
     *
     * <pre>{@code
     * // Open a UI
     * player.openUI(ui);
     *
     * // Later, close it
     * player.closeUI();
     * }</pre>
     */
    public void closeUI() {
        ImGuiLoader.removeViews(ImGuiLoader.getViews().toArray(new View[0]));
        MinecraftClient.getInstance().player.closeScreen();
    }

    public void sendToServer(String scriptName) {
        Dispatcher.sendToServer(new RunScriptPacket(scriptName, "server", new NbtCompound()));
    }

    public void sendToServer(String scriptName, String function) {
        Dispatcher.sendToServer(new RunScriptPacket(scriptName, function, new NbtCompound()));
    }

    public void sendToServer(String scriptName, String function, ScriptNbtCompound nbt) {
        Dispatcher.sendToServer(new RunScriptPacket(scriptName, function, nbt.getMinecraftNbtCompound()));
    }

    public void sendToServer(String scriptName, ScriptNbtCompound nbt) {
        Dispatcher.sendToServer(new RunScriptPacket(scriptName, nbt.getMinecraftNbtCompound()));
    }
}

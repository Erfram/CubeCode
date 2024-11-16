package com.cubecode.client.scripts.code.entities;

import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.screens.TestScreen;
import com.cubecode.client.scripts.code.ui.ClientCubeCodeUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientScriptPlayer extends ClientScriptEntity<ClientPlayerEntity> {
    public ClientScriptPlayer(ClientPlayerEntity player) {
        super(player);
    }

    public void send(String message) {
        this.entity.sendMessage(Text.of(message));
    }

    public ScriptVector getRotations() {
        return new ScriptVector(this.entity.getPitch(), this.entity.getYaw(), this.entity.getHeadYaw());
    }

    public void setRotations(float pitch, float yaw, float headYaw) {
        this.entity.setPitch(pitch);
        this.entity.setYaw(yaw);
        this.entity.setHeadYaw(headYaw);
    }

    public void setRotations(float pitch, float yaw, float headYaw, float bodyYaw) {
        this.entity.setPitch(pitch);
        this.entity.setYaw(yaw);
        this.entity.setHeadYaw(headYaw);
        this.entity.setBodyYaw(bodyYaw);
    }

    public void playSound(String sound, String soundCategory, float volume, float pitch) {
        this.entity.playSound(SoundEvent.of(new Identifier(sound)), SoundCategory.valueOf(soundCategory.toUpperCase()), volume, pitch);
    }

    public ScriptVector getResolution() {
        return new ScriptVector(MinecraftClient.getInstance().getWindow().getWidth(), MinecraftClient.getInstance().getWindow().getHeight(), 0);
    }

    public void openUI(ClientCubeCodeUI clientCubeCodeUI) {
        MinecraftClient.getInstance().setScreen(new TestScreen(true, true, clientCubeCodeUI.getView()));
    }

    public void openUI(ClientCubeCodeUI clientCubeCodeUI, boolean isBackground) {
        MinecraftClient.getInstance().setScreen(new TestScreen(isBackground, true, clientCubeCodeUI.getView()));
    }

    public void openUI(ClientCubeCodeUI clientCubeCodeUI, boolean isBackground, boolean shouldPause) {
        MinecraftClient.getInstance().setScreen(new TestScreen(isBackground, shouldPause, clientCubeCodeUI.getView()));
    }

    public void closeUI() {
        ImGuiLoader.removeViews(ImGuiLoader.getViews().toArray(new View[0]));
        MinecraftClient.getInstance().player.closeScreen();
    }
}

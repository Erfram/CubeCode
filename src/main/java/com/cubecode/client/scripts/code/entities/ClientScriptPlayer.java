package com.cubecode.client.scripts.code.entities;

import com.cubecode.api.scripts.code.ScriptVector;
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
        MinecraftClient.getInstance().player.closeScreen();
    }
}

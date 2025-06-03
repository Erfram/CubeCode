package com.cubecode;

import com.cubecode.network.Dispatcher;
import com.cubecode.state.PlayerState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static PlayerState playerState = new PlayerState();

    @Override
    public void onInitializeClient() {
        Dispatcher.register();
        EventHandlerClient.init();
    }
}
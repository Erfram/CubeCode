package com.cubecode;

import com.cubecode.api.scripts.Script;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.Dispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.concurrent.CopyOnWriteArrayList;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static CopyOnWriteArrayList<Script> scripts = new CopyOnWriteArrayList<>();

    @Override
    public void onInitializeClient() {
        Dispatcher.register();
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();
        CubeCodeConfig.loadConfig();
    }
}
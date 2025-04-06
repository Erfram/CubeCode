package com.cubecode;

import com.cubecode.scripting.ScriptExecutor;
import com.cubecode.client.gifs.GifManager;
import com.cubecode.client.image.ImageManager;
import com.cubecode.utils.Icons;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            GifManager.update();
        });

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            Icons.register();
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            //CubeCodeClient.windowStateManager = new WindowStateManager();

            CubeCodeClient.scriptExecutor = new ScriptExecutor();
            CubeCodeClient.imageManager = new ImageManager();
        });
    }
}
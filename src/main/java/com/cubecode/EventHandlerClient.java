package com.cubecode;

import com.cubecode.client.gifs.GifManager;
import com.cubecode.utils.Icons;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            GifManager.update();
        });
    }
}
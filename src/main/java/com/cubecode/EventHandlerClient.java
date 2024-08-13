package com.cubecode;

import com.cubecode.utils.Icons;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
    public static void init() {
        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            Icons.register();
        });
    }
}
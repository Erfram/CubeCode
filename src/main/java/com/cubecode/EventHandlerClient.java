package com.cubecode;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
    public static void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
        });
    }
}
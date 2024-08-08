package com.cubecode;

import com.cubecode.utils.FactoryType;
import com.cubecode.utils.Icons;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class EventHandlerClient {
    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            client.execute(() -> {
                for (FactoryType factoryType : FactoryType.values()) {
                    CubeCodeClient.factoryManagers.get(factoryType).unregister(CubeCodeClient.elementsLoaded.get(factoryType));
                }

                CubeCodeClient.elementsLoaded.clear();
            });
        });

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            Icons.register();
        });
    }
}
package com.cubecode;

import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.NetworkingPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkingPackets.registerS2CPackets();
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();
    }
}

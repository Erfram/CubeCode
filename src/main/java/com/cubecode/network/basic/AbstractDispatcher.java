package com.cubecode.network.basic;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public abstract class AbstractDispatcher {
    public abstract void register();
    protected void registerPacket(AbstractPacket packet, EnvType envType) {
        if (envType == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(packet.getIdentifier(), ((client, handler, buf, responseSender) -> {
                packet.toBytes(buf);

                client.execute(() -> {
                    packet.client(client, handler, responseSender);
                });
            }));
        } else  {
            ServerPlayNetworking.registerGlobalReceiver(packet.getIdentifier(), ((server, player, handler, buf, responseSender) -> {
                packet.toBytes(buf);

                server.execute(() -> {
                    packet.server(server, player, handler, responseSender);
                });
            }));
        }
    }
}

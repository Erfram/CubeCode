package com.cubecode.network.basic;

import com.cubecode.CubeCode;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public abstract class AbstractDispatcher {
    public abstract void register();

    protected <T extends AbstractPacket, A extends PacketHandler> void registerPacket(Class<T> packetClass, Class<A> packetHandler, EnvType envType) {
        try {
            T packet = packetClass.newInstance();
            if (envType == EnvType.CLIENT) {
                ClientPacketHandler<T> clientHandler = (ClientPacketHandler<T>)packetHandler.newInstance();
                ClientPlayNetworking.registerGlobalReceiver(packet.getIdentifier(), ((client, handler, buf, responseSender) -> {
                    packet.fromBytes(buf);

                    client.execute(() -> clientHandler.run(client, handler, responseSender, packet));
                }));
            } else {
                ServerPacketHandler<T> serverHandler = (ServerPacketHandler<T>)packetHandler.newInstance();
                ServerPlayNetworking.registerGlobalReceiver(packet.getIdentifier(), ((server, player, handler, buf, responseSender) -> {
                    packet.fromBytes(buf);

                    serverHandler.run(server, player, handler, responseSender, packet);
                }));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            CubeCode.LOGGER.error("can't create empty constructor packet. ", e);
            throw new RuntimeException(e);
        }
    }
}

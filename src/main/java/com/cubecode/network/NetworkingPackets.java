package com.cubecode.network;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.utils.FactoryType;
import com.cubecode.utils.PacketByteBufUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetworkingPackets {
    public static final Identifier REGISTRIES_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "registries_s2c_packet");
    public static final Identifier SYNC_INVENTORY_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "sync_inventory_s2c_packet");
    public static final Identifier REGISTRY_TEXTURE_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "registry_texture_s2c_packet");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(REGISTRIES_S2C_PACKET, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                String stringType = buf.readString();

                FactoryType factoryType = FactoryType.valueOf(stringType);
                List<String> serverElements = List.of(PacketByteBufUtils.readStringArray(buf));

                List<String> clientElements = CubeCodeClient.elementsLoaded.computeIfAbsent(factoryType, key -> new ArrayList<>());

                clientElements.addAll(serverElements);

                CubeCodeClient.elementsLoaded.put(
                        factoryType,
                        clientElements
                );

                CubeCodeClient.factoryManagers.get(factoryType).register(serverElements);
                CubeCode.LOGGER.info(String.format("type: %s | size: %s", factoryType, serverElements.size()));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SYNC_INVENTORY_S2C_PACKET, (client, handler, buf, responseSender) -> {
            int size = buf.readInt();

            client.execute(() -> {
                ClientPlayerEntity player = client.player;

                Int2ObjectMap<ItemStack> newInventory = new Int2ObjectOpenHashMap<>();

                for (int i = 0; i < size; i++) {
                    ItemStack bufStack = buf.readItemStack();
                    newInventory.put(i, bufStack);

                    ItemStack stack = newInventory.getOrDefault(i, ItemStack.EMPTY);
                    player.getInventory().setStack(i, stack);
                }

                for (int i = 0; i < player.getInventory().size(); i++) {
                    player.networkHandler.sendPacket(new PickFromInventoryC2SPacket(i));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(REGISTRY_TEXTURE_S2C_PACKET, (client, handler, buf, responseSender) -> {
            byte[] fileBytes = buf.readByteArray();

            client.execute(() -> {
                try {
                    NativeImage image = NativeImage.read(fileBytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}

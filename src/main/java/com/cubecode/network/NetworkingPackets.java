package com.cubecode.network;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import com.cubecode.api.scripts.Script;
import com.cubecode.api.scripts.ScriptManager;
import com.cubecode.api.scripts.code.ScriptEvent;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.api.scripts.code.ScriptServer;
import com.cubecode.api.scripts.code.ScriptWorld;
import com.cubecode.api.scripts.code.entities.ScriptEntity;
import com.cubecode.api.utils.FileManager;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.views.textEditor.TextEditorView;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.FactoryType;
import com.cubecode.utils.PacketByteBufUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkingPackets {
    public static final Identifier REGISTRIES_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "registries_s2c_packet");
    public static final Identifier SYNC_INVENTORY_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "sync_inventory_s2c_packet");
    public static final Identifier REGISTRY_TEXTURE_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "registry_texture_s2c_packet");
    public static final Identifier UPDATE_SCRIPTS_S2C_PACKET = new Identifier(CubeCode.MOD_ID, "update_scripts_s2c_packet");

    public static final Identifier CREATE_ELEMENT_FILE_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "create_element_file_c2s_packet");
    public static final Identifier RUN_SCRIPT_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "run_script_c2s_packet");
    public static final Identifier CREATE_SCRIPT_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "create_script_c2s_packet");
    public static final Identifier RENAME_SCRIPT_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "rename_script_c2s_packet");
    public static final Identifier UPDATE_SCRIPTS_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "update_scripts_c2s_packet");
    public static final Identifier SAVE_SCRIPT_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "save_script_c2s_packet");
    public static final Identifier DELETE_SCRIPT_C2S_PACKET = new Identifier(CubeCode.MOD_ID, "delete_script_c2s_packet");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(RENAME_SCRIPT_C2S_PACKET, ((server, player, handler, buf, responseSender) -> {
            String oldName = buf.readString();
            String newName = buf.readString();

            newName = newName.endsWith(".js") ? newName : newName + ".js";

            CubeCode.scriptManager.renameFile(oldName, newName);
            updateScriptsC2SPacket(player);
        }));

        ServerPlayNetworking.registerGlobalReceiver(DELETE_SCRIPT_C2S_PACKET, ((server, player, handler, buf, responseSender) -> {
            String script = buf.readString();

            CubeCode.scriptManager.deleteScript(script);
            updateScriptsC2SPacket(player);
        }));

        ServerPlayNetworking.registerGlobalReceiver(CREATE_SCRIPT_C2S_PACKET, ((server, player, handler, buf, responseSender) -> {
            String name = buf.readString();
            String code = CubeCodeConfig.getScriptConfig().contextName + ".server.send(\"Hellow World!\", false)";

            name = name.endsWith(".js") ? name : name + ".js";

            CubeCode.scriptManager.createScript(name, code);
            updateScriptsC2SPacket(player);
        }));

        ServerPlayNetworking.registerGlobalReceiver(SAVE_SCRIPT_C2S_PACKET, ((server, player, handler, buf, responseSender) -> {
            String script = buf.readString();
            String code = buf.readString();

            CubeCode.scriptManager.setScript(script, code);
            updateScriptsC2SPacket(player);
        }));

        ServerPlayNetworking.registerGlobalReceiver(CREATE_ELEMENT_FILE_C2S_PACKET, (server, player, handler, buf, responseSender) -> {
            String path = CubeCode.cubeCodeDirectory.getPath() + buf.readString();
            String elementJson = buf.readString();

            FileManager.writeJsonToFile(path, elementJson);
        });

        ServerPlayNetworking.registerGlobalReceiver(RUN_SCRIPT_C2S_PACKET, (server, player, handler, buf, responseSender) -> {
            String code = buf.readString();

            HashMap<String, Object> properties = new HashMap<>();
            properties.put(CubeCodeConfig.getScriptConfig().contextName, new ScriptEvent(
                    null,
                    null,
                    ScriptEntity.create(player),
                    null,
                    new ScriptWorld(player.getWorld()),
                    new ScriptServer(server)
            ));

            properties.put("CubeCode", new ScriptFactory());

            try {
                ScriptManager.evalCode(code, 0, "eval", properties);
            } catch (CubeCodeException cce) {
                player.sendMessage(Text.of(cce.getMessage()));
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(UPDATE_SCRIPTS_C2S_PACKET, (server, player, handler, buf, responseSender) -> {
            updateScriptsC2SPacket(player);
        });
    }

    public static void updateScriptsC2SPacket(ServerPlayerEntity player) {
        CubeCode.scriptManager.updateScripts();
        Map<String, Script> scripts = CubeCode.scriptManager.getScripts();
        PacketByteBuf byteBuf = PacketByteBufs.create();

        List<String> scriptsCode = new ArrayList<>();

        scripts.values().forEach((script) -> {
            scriptsCode.add(script.code);
        });

        byteBuf.writeCollection(
                scripts.keySet().stream().toList(),
                PacketByteBuf::writeString
        );

        byteBuf.writeCollection(
                scriptsCode,
                PacketByteBuf::writeString
        );

        ServerPlayNetworking.send(player, UPDATE_SCRIPTS_S2C_PACKET, byteBuf);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_SCRIPTS_S2C_PACKET, ((client, handler, buf, responseSender) -> {
            List<String> scriptList = buf.readList(PacketByteBuf::readString);
            List<String> codes = buf.readList(PacketByteBuf::readString);
            client.execute(() -> {
                Map<String, Script> scripts = new HashMap<>();

                for (int i = 0; i < scriptList.size(); i++) {
                    scripts.put(scriptList.get(i), new Script(codes.get(i)));
                }

                CubeCodeClient.scripts = scripts;

                TextEditorView.scriptsName = CubeCodeClient.scripts.keySet().stream().toList();
                TextEditorView.scripts = CubeCodeClient.scripts.values().stream().toList();
            });
        }));

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

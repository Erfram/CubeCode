package com.cubecode;

import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.effect.EffectManager;
import com.cubecode.api.factory.enchantment.EnchantmentManager;
import com.cubecode.api.factory.item.ItemManager;
import com.cubecode.api.factory.material.ToolMaterialManager;
import com.cubecode.api.factory.potion.PotionManager;
import com.cubecode.api.scripts.Script;
import com.cubecode.client.config.ConfigManager;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.NetworkingPackets;
import com.cubecode.utils.FactoryType;
import com.cubecode.utils.Icons;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static Map<String, Script> scripts = new HashMap<>();
    public static final Map<FactoryType, FactoryManager> factoryManagers = new HashMap<>();
    public static Map<FactoryType, List<String>> elementsLoaded = new HashMap<>();

    static {
        factoryManagers.put(FactoryType.BLOCK, new BlockManager());
        factoryManagers.put(FactoryType.ITEM, new ItemManager());
        factoryManagers.put(FactoryType.TOOL_MATERIAL, new ToolMaterialManager());
        factoryManagers.put(FactoryType.POTION, new PotionManager());
        factoryManagers.put(FactoryType.EFFECT, new EffectManager());
        factoryManagers.put(FactoryType.ENCHANTMENT, new EnchantmentManager());
    }

    @Override
    public void onInitializeClient() {
        NetworkingPackets.registerS2CPackets();
        Dispatcher.register();
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();
        CubeCodeConfig.loadConfig();
    }
}
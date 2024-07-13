package com.cubecode;

import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.effect.EffectManager;
import com.cubecode.api.factory.enchantment.EnchantmentManager;
import com.cubecode.api.factory.item.ItemManager;
import com.cubecode.api.factory.material.ToolMaterialManager;
import com.cubecode.api.factory.potion.PotionManager;
import com.cubecode.content.CubeCodeKeyBindings;
import com.cubecode.network.NetworkingPackets;
import com.cubecode.utils.FactoryType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static final HashMap<FactoryType, FactoryManager> factoryManagers = new HashMap<>();
    public static HashMap<FactoryType, List<String>> elementsLoaded = new HashMap<>();

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
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();
    }
}
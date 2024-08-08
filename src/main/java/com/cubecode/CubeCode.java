package com.cubecode;

import com.cubecode.api.factory.TextureManager;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.factory.effect.EffectManager;
import com.cubecode.api.factory.enchantment.EnchantmentManager;
import com.cubecode.api.factory.item.ItemManager;
import com.cubecode.api.factory.material.ToolMaterialManager;
import com.cubecode.api.factory.potion.PotionManager;
import com.cubecode.network.Dispatcher;
import com.cubecode.network.NetworkingPackets;
import com.cubecode.utils.FactoryType;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import com.cubecode.api.scripts.ScriptManager;

import java.io.File;
import java.util.LinkedHashMap;

public class CubeCode implements ModInitializer {
    public static final Logger LOGGER;

    public static final String MOD_ID;

    public static File cubeCodeDirectory;
    public static File factoryDirectory;
    public static File contentDirectory;

    public static ScriptManager scriptManager;
    public static TextureManager textureManager;
    public static FactoryManager factoryManager;
    public static BlockManager blockManager;
    public static ItemManager itemManager;
    public static ToolMaterialManager toolMaterialManager;
    public static PotionManager potionManager;
    public static EffectManager effectManager;
    public static EnchantmentManager enchantmentManager;
    public static LinkedHashMap<FactoryType, FactoryManager> factoryManagers;

    static {
        LOGGER = LogUtils.getLogger();
        MOD_ID = "cubecode";
    }

    @Override
    public void onInitialize() {
        EventHandler.init();
        Dispatcher.register();
        NetworkingPackets.registerC2SPackets();
    }
}
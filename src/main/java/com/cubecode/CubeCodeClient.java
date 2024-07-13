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
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.*;

@Environment(EnvType.CLIENT)
public class CubeCodeClient implements ClientModInitializer {
    public static final HashMap<FactoryType, FactoryManager> factoryManagers = new HashMap<>();

    static {
        factoryManagers.put(FactoryType.BLOCK, new BlockManager());
        factoryManagers.put(FactoryType.ITEM, new ItemManager());
        factoryManagers.put(FactoryType.TOOL_MATERIAL, new ToolMaterialManager());
        factoryManagers.put(FactoryType.POTION, new PotionManager());
        factoryManagers.put(FactoryType.EFFECT, new EffectManager());
        factoryManagers.put(FactoryType.ENCHANTMENT, new EnchantmentManager());
    }

    public static HashMap<FactoryType, List<String>> elementsLoaded = new HashMap<>();

    @Override
    public void onInitializeClient() {
        NetworkingPackets.registerS2CPackets();
        CubeCodeKeyBindings.init();
        EventHandlerClient.init();


        try {
            List<ModResourcePack> packs = new ArrayList<>();

            Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(CubeCode.MOD_ID);

            ModNioResourcePack pack = ModNioResourcePack.create(
                    container.get().getMetadata().getId(), container.get(),
                    "F:\\resource", ResourceType.CLIENT_RESOURCES, ResourcePackActivationType.ALWAYS_ENABLED,
                    true
            );


            //pack.open(ResourceType.CLIENT_RESOURCES, );


            MinecraftClient.getInstance().reloadResources();


//            container.bindMetadata(MetadataCollection.from(null, ""));
//            MinecraftClient.getInstance().resource.addModAsResource(container);
//            FMLClientHandler.instance().refreshResources();
//            LOGGER.info("######### Dynamic Ambience And Music successfully loaded #########");
        } catch (Exception e) {
            e.printStackTrace();
            CubeCode.LOGGER.info(e.getMessage());
        }
        //RegionHandler.loadSounds();
    }
}
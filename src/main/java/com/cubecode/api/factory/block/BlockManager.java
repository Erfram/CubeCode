package com.cubecode.api.factory.block;

import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GSONManager;
import com.cubecode.utils.CubeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.registry.Registries;

import java.io.File;

public class BlockManager extends FactoryManager {

    public static class DefaultBlock {
        public String id;
        public String name;
        public String texture;
        public float hardness;
        public boolean isCreateItemBlock;
    }

    public BlockManager(File directory) {
        super(directory);
    }

    public void registryBlocks() {
        CubeRegistry<?> blockRegistry = (CubeRegistry<?>) Registries.BLOCK;
        CubeRegistry<?> itemRegistry = (CubeRegistry<?>) Registries.ITEM;

        blockRegistry.unFreeze();
        itemRegistry.unFreeze();

        for (File fileBlock : this.getFiles()) {
            DefaultBlock defaultBlock = GSONManager.readJSON(fileBlock, DefaultBlock.class);

            FabricBlockSettings blockSettings = FabricBlockSettings.create();
            blockSettings.hardness(defaultBlock.hardness);

            ExampleBlock block = new ExampleBlock(blockSettings);

            registerBlock(blockRegistry, defaultBlock.id, block);

            if (defaultBlock.isCreateItemBlock) {
                registerBlockItem(itemRegistry, defaultBlock.id, block);
            }
        }

        blockRegistry.freeze();
        itemRegistry.freeze();
    }
}
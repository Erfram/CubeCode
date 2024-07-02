package com.cubecode.api.factory.block;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GSONManager;
import com.cubecode.utils.CubeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.ArrayList;

public class BlockManager extends FactoryManager {
    private static final ArrayList<String> blocksToRemove = new ArrayList<>();

    public BlockManager(File directory) {
        super(directory);
    }

    public ArrayList<String> getBlocksToRemove() {
        return blocksToRemove;
    }

    public void registerBlocks() {
        CubeRegistry<?> blockRegistry = (CubeRegistry<?>) Registries.BLOCK;
        CubeRegistry<?> itemRegistry = (CubeRegistry<?>) Registries.ITEM;

        blockRegistry.unFreeze();
        itemRegistry.unFreeze();

        for (File fileBlock : this.getFiles()) {
            DefaultBlock defaultBlock = GSONManager.readJSON(fileBlock, DefaultBlock.class);

            FabricBlockSettings blockSettings = getFabricBlockSettings(defaultBlock);

            ExampleBlock block = new ExampleBlock(blockSettings);

            registerBlock(blockRegistry, defaultBlock.id, block);

            if (defaultBlock.isCreateItemBlock) {
                registerBlockItem(itemRegistry, defaultBlock.id, block);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static void registerBlocksClient(String[] blocks) {
        CubeRegistry<?> blockRegistry = (CubeRegistry<?>) Registries.BLOCK;
        CubeRegistry<?> itemRegistry = (CubeRegistry<?>) Registries.ITEM;

        blockRegistry.unFreeze();
        itemRegistry.unFreeze();

        for (String jsonBlock : blocks) {
            BlockManager.DefaultBlock defaultBlock = GSONManager.readJSON(jsonBlock, BlockManager.DefaultBlock.class);

            FabricBlockSettings blockSettings = BlockManager.getFabricBlockSettings(defaultBlock);

            ExampleBlock block = new ExampleBlock(blockSettings);

            BlockManager.registerBlock(blockRegistry, defaultBlock.id, block);

            if (defaultBlock.isCreateItemBlock) {
                BlockManager.registerBlockItem(itemRegistry, defaultBlock.id, block);
            }
        }
    }

    public void unregisterBlocks() {
        for (String block : blocksToRemove) {
            CubeRegistry blockRegistry = (CubeRegistry<?>) Registries.BLOCK;
            CubeRegistry itemRegistry = (CubeRegistry<?>) Registries.ITEM;
            DefaultBlock defaultBlock = GSONManager.readJSON(this.getFile(block), DefaultBlock.class);

            blockRegistry.unFreeze();
            itemRegistry.unFreeze();
            blockRegistry.remove(Registries.BLOCK, new Identifier(CubeCode.MOD_ID, defaultBlock.id));
            itemRegistry.remove(Registries.ITEM, new Identifier(CubeCode.MOD_ID, defaultBlock.id));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void unregisterBlocksClient(String[] blocks) {
        for (String block : blocks) {
            CubeRegistry blockRegistry = (CubeRegistry<?>) Registries.BLOCK;
            CubeRegistry itemRegistry = (CubeRegistry<?>) Registries.ITEM;
            DefaultBlock defaultBlock = GSONManager.readJSON(block, DefaultBlock.class);

            blockRegistry.unFreeze();
            itemRegistry.unFreeze();
            blockRegistry.remove(Registries.BLOCK, new Identifier(CubeCode.MOD_ID, defaultBlock.id));
            itemRegistry.remove(Registries.ITEM, new Identifier(CubeCode.MOD_ID, defaultBlock.id));
        }
    }

    public static FabricBlockSettings getFabricBlockSettings(DefaultBlock defaultBlock) {
        FabricBlockSettings blockSettings = FabricBlockSettings.create();
        String[] splitIdentifier = defaultBlock.dropsLike.split(":", 2);

        blockSettings.nonOpaque();
        blockSettings.requiresTool();
        blockSettings.collidable(defaultBlock.collision);
        blockSettings.slipperiness(defaultBlock.slipperiness);
        blockSettings.luminance(defaultBlock.luminance);
        blockSettings.resistance(defaultBlock.resistance);
        blockSettings.hardness(defaultBlock.hardness);
        blockSettings.jumpVelocityMultiplier(defaultBlock.jumpVelocityMultiplier);
        blockSettings.velocityMultiplier(defaultBlock.velocityMultiplier);
        blockSettings.dynamicBounds();
        blockSettings.breakInstantly();

        if (!defaultBlock.dropsLike.isEmpty())
            blockSettings.dropsLike(Registries.BLOCK.get(new Identifier(splitIdentifier[0], splitIdentifier[1])));
        if (defaultBlock.solid)
            blockSettings.solid();
        if (defaultBlock.burnable)
            blockSettings.burnable();
        if (!defaultBlock.blockBreakParticles)
            blockSettings.noBlockBreakParticles();
        if (defaultBlock.replaceable)
            blockSettings.replaceable();
        if (defaultBlock.breakInstantly)
            blockSettings.breakInstantly();

        return blockSettings;
    }

    public static class DefaultBlock {
        public String id;
        public String name;
        public String texture;
        public String dropsLike;
        public float hardness;
        public float resistance;
        public float slipperiness;
        public float jumpVelocityMultiplier;
        public float velocityMultiplier;
        public int luminance;
        public boolean isCreateItemBlock;
        public boolean solid;
        public boolean collision;
        public boolean replaceable;
        public boolean burnable;
        public boolean blockBreakParticles;
        public boolean breakInstantly;
    }
}
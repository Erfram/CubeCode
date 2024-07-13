package com.cubecode.api.factory.block;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GSONManager;
import com.cubecode.utils.CubeRegistry;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.DynamicTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.io.File;
import java.util.List;

public class BlockManager extends FactoryManager {
    public BlockManager(File directory) {
        super(directory);
    }

    public BlockManager() {
        super();
    }

    @Override
    public void register(List<String> blocks) {
        CubeRegistry<Block> blockRegistry = (CubeRegistry<Block>) Registries.BLOCK;
        CubeRegistry<Item> itemRegistry = (CubeRegistry<Item>) Registries.ITEM;

        blockRegistry.unFreeze();
        itemRegistry.unFreeze();

        for (String jsonBlock : blocks) {
            var defaultBlock = GSONManager.readJSON(jsonBlock, DefaultBlock.class);

            this.registerBlockAndItem(defaultBlock);
        }
    }

    @Override
    public void unregister(List<String> blocks) {
        CubeRegistry<Block> blockRegistry = (CubeRegistry<Block>) Registries.BLOCK;
        CubeRegistry<Item> itemRegistry = (CubeRegistry<Item>) Registries.ITEM;

        blockRegistry.unFreeze();
        itemRegistry.unFreeze();

        for (String jsonBlock : blocks) {
            var defaultBlock = GSONManager.readJSON(jsonBlock, DefaultBlock.class);

            blockRegistry.remove(Registries.BLOCK, new Identifier(CubeCode.MOD_ID, defaultBlock.id));

            if (defaultBlock.isCreateItemBlock) {
                itemRegistry.remove(Registries.ITEM, new Identifier(CubeCode.MOD_ID, defaultBlock.id));
            }
        }
    }

    private void registerBlockAndItem(BlockManager.DefaultBlock defaultBlock) {
        FabricBlockSettings blockSettings = getFabricBlockSettings(defaultBlock);
        CubeBlock block = new CubeBlock(blockSettings);

        this.registerElement(Registries.BLOCK, defaultBlock.id, block);

        if (defaultBlock.isCreateItemBlock) {
            CubeBlockItem blockItem = new CubeBlockItem(block, new FabricItemSettings(), defaultBlock.name, defaultBlock.description);
            this.registerElement(Registries.ITEM, defaultBlock.id, blockItem);
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
        public String description;
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

    public void modifyingChunks(MinecraftServer server) {
        for (String jsonBlock : CubeCode.blockManager.getElementsToRemove()) {
            BlockManager.DefaultBlock defaultBlock = GSONManager.readJSON(jsonBlock, BlockManager.DefaultBlock.class);

            for (ServerWorld world : server.getWorlds()) {
                LongIterator longIterator = world.getForcedChunks().longIterator();
                int changedBlocks = 0;
                boolean isChunkUpdateSuspended = false;

                while (longIterator.hasNext()) {
                    long lng = longIterator.nextLong();
                    ChunkPos chunkPos = new ChunkPos(lng);

                    WorldChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
                    int startX = chunkPos.getStartX();
                    int startZ = chunkPos.getStartZ();
                    int startY = chunk.getBottomY();

                    for (int x = startX; x < chunkPos.getEndX(); x++) {
                        for (int z = startZ; z < chunkPos.getEndZ(); z++) {
                            for (int y = startY; y < chunk.getTopY(); y++) {
                                FabricBlockSettings blockSettings = BlockManager.getFabricBlockSettings(defaultBlock);
                                CubeBlock block = new CubeBlock(blockSettings);
                                BlockPos blockPos = new BlockPos(x, y, z);

                                if (chunk.getBlockState(blockPos).getBlock().equals(block)) {
                                    if (!isChunkUpdateSuspended && changedBlocks >= 50) {
                                        chunk.setNeedsSaving(true);
                                        isChunkUpdateSuspended = true;
                                    }
                                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NO_REDRAW);
                                    changedBlocks++;
                                }
                            }
                        }
                    }

                    if (isChunkUpdateSuspended || changedBlocks > 0) {
                        chunk.setNeedsSaving(true);
                        world.getChunkManager().markForUpdate(chunkPos.getCenterAtY(50));
                    }
                }
            }
        }
    }
}
package com.cubecode.api.factory;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.block.BlockManager;
import com.cubecode.api.factory.block.ExampleBlock;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.api.utils.GSONManager;
import com.cubecode.utils.CubeRegistry;
import it.unimi.dsi.fastutil.longs.LongIterator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FactoryManager extends DirectoryManager {
    public FactoryManager(File directory) {
        super(directory);
    }

    public static void registerBlock(CubeRegistry registry, String id, Block block) {
        Block existingBlock = Registries.BLOCK.get(new Identifier(CubeCode.MOD_ID, id));
        int rawId = registry.getNextId();

        if(existingBlock instanceof ExampleBlock) {
            rawId = Registries.BLOCK.getRawId(existingBlock) == -1 ? registry.getNextId() : Registries.BLOCK.getRawId(existingBlock);
        }

        registry.set(rawId, Registries.BLOCK, new Identifier(CubeCode.MOD_ID, id), block);
    }

    public static void registerBlockItem(CubeRegistry registry, String id, Block block) {
        Item existingItem = Registries.ITEM.get(new Identifier(CubeCode.MOD_ID, id));
        int rawId = registry.getNextId();

        if(!(existingItem instanceof AirBlockItem) || existingItem instanceof BlockItem) {
            rawId = Registries.ITEM.getRawId(existingItem) == -1 ? registry.getNextId() : Registries.ITEM.getRawId(existingItem);
        }

        registry.set(rawId, Registries.ITEM, new Identifier(CubeCode.MOD_ID, id), new BlockItem(block, new FabricItemSettings()));
    }

    public static String[] getBlockStringsFromFiles(ArrayList<File> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return new String[0];
        }

        List<String> blockStrings = new ArrayList<>();

        for (File block : blocks) {
            try {
                String content = FileUtils.readFileToString(block);
                if (content != null && !content.isEmpty()) {
                    blockStrings.add(content);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + block.getName() + ". " + e.getMessage());
            }
        }

        return blockStrings.toArray(new String[0]);
    }

    public static void modifyingChunks(MinecraftServer server) {
        for (ServerWorld world : server.getWorlds()) {
            LongIterator longIterator = world.getForcedChunks().longIterator();
            int changedBlocks = 0;
            boolean isChunkUpdateSuspended = false;
            for (File fileBlock : CubeCode.blockManager.getFiles()) {
                BlockManager.DefaultBlock defaultBlock = GSONManager.readJSON(fileBlock, BlockManager.DefaultBlock.class);

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
                                ExampleBlock block = new ExampleBlock(blockSettings);
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

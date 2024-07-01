package com.cubecode.api.factory;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.block.ExampleBlock;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.utils.CubeRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
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

    public static String[] getBlockStringsFromFiles(File[] blocks) {
        if (blocks == null || blocks.length == 0) {
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
}

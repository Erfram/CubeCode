package com.cubecode.api.factory.item;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GsonManager;
import com.cubecode.utils.CubeRegistry;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.io.File;
import java.util.List;

public class ItemManager extends FactoryManager {
    public ItemManager(File directory) {
        super(directory);
    }

    public ItemManager() {
        super();
    }

    @Override
    public void register(List<String> items) {
        CubeRegistry<Item> registry = (CubeRegistry<Item>) Registries.ITEM;

        registry.unFreeze();
        for (String jsonItem : items) {
            ItemManager.DefaultItem defaultItem = GsonManager.readJSON(jsonItem, ItemManager.DefaultItem.class);

            Item.Settings itemSettings = this.getItemSettings(defaultItem);

            Item item = new CubeItem(itemSettings, defaultItem.name, defaultItem.description);

            if (defaultItem.isTool) {
                ToolMaterial toolMaterial;
                try {
                    toolMaterial = ToolMaterials.valueOf(defaultItem.toolMaterial.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    toolMaterial = CubeCode.toolMaterialManager.getToolMaterial(defaultItem.toolMaterial);
                }

                item = new CubeToolItem(toolMaterial, itemSettings, defaultItem.name, defaultItem.description);

                if (defaultItem.isMiningTool) {
                    item = new CubeMiningToolItem(
                            defaultItem.attackDamage,
                            defaultItem.attackSpeed,
                            toolMaterial,
                            TagKey.of(RegistryKeys.BLOCK, new Identifier(defaultItem.tag)),
                            this.getItemSettings(defaultItem),
                            defaultItem.name,
                            defaultItem.description
                    );
                }
            }

            this.registerElement(Registries.ITEM, defaultItem.id, item);
        }
    }

    @Override
    public void unregister(List<String> items) {
        super.unregister(items);
        CubeRegistry<Item> registry = (CubeRegistry<Item>) Registries.ITEM;
        registry.unFreeze();
        for (String jsonItem : items) {
            var defaultItem = GsonManager.readJSON(jsonItem, ItemManager.DefaultItem.class);

            registry.remove(Registries.ITEM, new Identifier(CubeCode.MOD_ID, defaultItem.id));
        }
    }

    public Item.Settings getItemSettings(ItemManager.DefaultItem defaultItem) {
        Item.Settings itemSettings = new Item.Settings();
        itemSettings.maxCount(defaultItem.maxCount);
        itemSettings.maxDamage(defaultItem.maxDurability);
        itemSettings.rarity(Rarity.valueOf(defaultItem.rarity.toUpperCase()));

        if (defaultItem.fireproof)
            itemSettings.fireproof();

        if (defaultItem.isFood) {
            FoodComponent.Builder builder = new FoodComponent.Builder();
            builder.hunger(defaultItem.hunger);
            builder.saturationModifier(defaultItem.saturationModifier);

            if (defaultItem.snack)
                builder.snack();
            if(defaultItem.alwaysEdible)
                builder.alwaysEdible();
            if(defaultItem.meat)
                builder.meat();

            itemSettings.food(builder.build());
        }

        return itemSettings;
    }

    public static class DefaultItem {
        public String id;
        public String name;
        public String description;
        public String texture;

        public String rarity;
        public int maxCount;
        public int maxDurability;
        public boolean fireproof;

        public boolean isFood;
        public int saturationModifier;
        public int hunger;
        public boolean snack;
        public boolean alwaysEdible;
        public boolean meat;

        public boolean isTool;
        public String toolMaterial;

        public boolean isMiningTool;
        public float attackDamage;
        public float attackSpeed;
        public String tag;
    }
}

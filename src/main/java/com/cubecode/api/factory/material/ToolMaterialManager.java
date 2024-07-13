package com.cubecode.api.factory.material;

import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GSONManager;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolMaterialManager extends FactoryManager {
    public HashMap<String, ToolMaterial> toolMaterials = new HashMap<>();

    public ToolMaterialManager(File directory) {
        super(directory);
    }

    public ToolMaterialManager() {
        super();
    }

    public ToolMaterial getToolMaterial(String materialName) {
        return toolMaterials.get(materialName);
    }

    @Override
    public void register(List<String> toolMaterials) {
        for (String jsonToolMaterial : toolMaterials) {
            var defaultToolMaterial = GSONManager.readJSON(jsonToolMaterial, ToolMaterialManager.DefaultToolMaterial.class);

            final ArrayList<Item> items = new ArrayList<>();

            for (String item : defaultToolMaterial.items) {
                items.add(Registries.ITEM.get(new Identifier(item)));
            }

            this.toolMaterials.put(defaultToolMaterial.id, this.create(
                    defaultToolMaterial.miningLevel,
                    defaultToolMaterial.durability,
                    defaultToolMaterial.miningSpeed,
                    defaultToolMaterial.attackDamage,
                    defaultToolMaterial.enchantability,
                    items
            ));
        }
    }

    public ToolMaterial create(int miningLevel, int durability, float miningSpeed, float attackDamage, int enchantability, ArrayList<Item> items) {
        return new ToolMaterial() {
            @Override
            public int getDurability() {
                return durability;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return miningSpeed;
            }

            @Override
            public float getAttackDamage() {
                return attackDamage;
            }

            @Override
            public int getMiningLevel() {
                return miningLevel;
            }

            @Override
            public int getEnchantability() {
                return enchantability;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(items.toArray(new Item[0]));
            }
        };
    }

    public static class DefaultToolMaterial {
        public String id;

        public int durability;
        public float miningSpeed;
        public float attackDamage;
        public int miningLevel;
        public int enchantability;
        public String[] items;
    }
}
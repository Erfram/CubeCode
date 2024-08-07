package com.cubecode.api.factory.enchantment;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GsonManager;
import com.cubecode.utils.CubeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.List;

public class EnchantmentManager extends FactoryManager {
    public EnchantmentManager(File directory) {
        super(directory);
    }

    public EnchantmentManager() {
        super();
    }

    @Override
    public void register(List<String> enchantments) {
        CubeRegistry<Enchantment> registry = (CubeRegistry<Enchantment>) Registries.ENCHANTMENT;

        registry.unFreeze();
        for (String jsonEnchantment : enchantments) {
            var defaultEnchantment = GsonManager.readJSON(jsonEnchantment, DefaultEnchantment.class);

            EquipmentSlot[] slotTypes = new EquipmentSlot[defaultEnchantment.slotTypes.length];
            for (int i = 0; i < defaultEnchantment.slotTypes.length; i++) {
                slotTypes[i] = EquipmentSlot.valueOf(defaultEnchantment.slotTypes[i].toUpperCase());
            }

            Enchantment enchantment = new CubeEnchantment(
                    Enchantment.Rarity.valueOf(defaultEnchantment.rarity.toUpperCase()),
                    EnchantmentTarget.valueOf(defaultEnchantment.enchantmentTarget.toUpperCase()),
                    slotTypes,
                    defaultEnchantment.minLevel,
                    defaultEnchantment.maxLevel,
                    null, //TODO SCRIPTs
                    null,
                    defaultEnchantment.name
            );

            this.registerElement(Registries.ENCHANTMENT, defaultEnchantment.id, enchantment);
        }
    }

    @Override
    public void unregister(List<String> enchantments) {
        CubeRegistry<Enchantment> registry = (CubeRegistry<Enchantment>) Registries.ENCHANTMENT;

        registry.unFreeze();

        for (String jsonEnchantment : enchantments) {
            var defaultEnchantment = GsonManager.readJSON(jsonEnchantment, DefaultEnchantment.class);

            registry.remove(Registries.ENCHANTMENT, new Identifier(CubeCode.MOD_ID, defaultEnchantment.id));
        }
    }

    public static class DefaultEnchantment {
        public String id;
        public String name;

        public String rarity;
        public String enchantmentTarget;
        public String[] slotTypes;
        public int minLevel;
        public int maxLevel;
        public String onTargetDamagedScript;
        public String onUserDamagedScript;
    }
}

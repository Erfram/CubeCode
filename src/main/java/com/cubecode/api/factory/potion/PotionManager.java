package com.cubecode.api.factory.potion;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GsonManager;
import com.cubecode.utils.CubeRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.List;

public class PotionManager extends FactoryManager {
    public PotionManager(File directory) {
        super(directory);
    }

    public PotionManager() {
        super();
    }

    @Override
    public void register(List<String> potions) {
        CubeRegistry<Potion> registry = (CubeRegistry<Potion>) Registries.POTION;

        registry.unFreeze();
        for (String jsonPotion : potions) {
            var defaultPotion = GsonManager.readJSON(jsonPotion, PotionManager.DefaultPotion.class);
            StatusEffectInstance statusEffect = new StatusEffectInstance(
                    Registries.STATUS_EFFECT.get(new Identifier(defaultPotion.effect)),
                    defaultPotion.duration,
                    defaultPotion.amplifier,
                    defaultPotion.ambient,
                    defaultPotion.showParticles,
                    defaultPotion.showIcon);

            CubePotion cubePotion = new CubePotion(statusEffect);

            this.registerElement(Registries.POTION, defaultPotion.id, cubePotion);
        }
    }

    @Override
    public void unregister(List<String> potions) {
        CubeRegistry<Potion> registry = (CubeRegistry<Potion>) Registries.POTION;
        registry.unFreeze();

        for (String jsonPotion : potions) {
            var defaultPotion = GsonManager.readJSON(jsonPotion, PotionManager.DefaultPotion.class);

            registry.remove(Registries.POTION, new Identifier(CubeCode.MOD_ID, defaultPotion.id));
        }
    }

    public static class DefaultPotion {
        public String id;
        public String name;
        public String description;
        public String effect;
        public int duration;
        public int amplifier;
        public boolean ambient;
        public boolean showParticles;
        public boolean showIcon;
    }
}

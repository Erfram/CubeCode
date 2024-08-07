package com.cubecode.api.factory.effect;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.utils.GsonManager;
import com.cubecode.utils.CubeRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.List;

public class EffectManager extends FactoryManager {
    public EffectManager(File directory) {
        super(directory);
    }

    public EffectManager() {
        super();
    }

    @Override
    public void register(List<String> effects) {
        CubeRegistry<StatusEffect> registry = (CubeRegistry<StatusEffect>) Registries.STATUS_EFFECT;

        registry.unFreeze();
        for (String jsonEffect : effects) {
            var defaultEffect = GsonManager.readJSON(jsonEffect, EffectManager.DefaultEffect.class);

            CubeEffect cubeEffect = new CubeEffect(StatusEffectCategory.valueOf(defaultEffect.category.toUpperCase()), defaultEffect.color, defaultEffect.name);

            this.registerElement(Registries.STATUS_EFFECT, defaultEffect.id, cubeEffect);
        }
    }

    @Override
    public void unregister(List<String> effects) {
        CubeRegistry<StatusEffect> registry = (CubeRegistry<StatusEffect>) Registries.STATUS_EFFECT;

        registry.unFreeze();

        for (String jsonEffect : effects) {
            var defaultEffect = GsonManager.readJSON(jsonEffect, EffectManager.DefaultEffect.class);

            registry.remove(Registries.STATUS_EFFECT, new Identifier(CubeCode.MOD_ID, defaultEffect.id));
        }
    }

    public static class DefaultEffect {
        public String id;
        public String name;

        public String category;
        public int color;
        public String script;
    }
}
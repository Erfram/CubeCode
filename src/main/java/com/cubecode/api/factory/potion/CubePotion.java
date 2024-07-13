package com.cubecode.api.factory.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;

public class CubePotion extends Potion {
    public CubePotion(String baseName, StatusEffectInstance... effects) {
        super(baseName, effects);
    }

    public CubePotion(StatusEffectInstance... effects) {
        super(effects);
    }
}

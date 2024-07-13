package com.cubecode.api.factory.effect;

import com.cubecode.api.scripts.Script;
import com.cubecode.utils.TextUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.text.Text;

public class CubeEffect extends StatusEffect {
    Script script = new Script("");
    String name;
    public CubeEffect(StatusEffectCategory category, int color, Script script) {
        super(category, color);
        this.script = script;
    }

    public CubeEffect(StatusEffectCategory category, int color, String name) {
        super(category, color);
        this.name = name;
    }

    @Override
    public Text getName() {
        return TextUtils.formatText(this.name);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the effect every tick
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        //TODO Добавить сюда скриптить запускаться
    }
}
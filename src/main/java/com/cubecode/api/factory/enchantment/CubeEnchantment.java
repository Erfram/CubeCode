package com.cubecode.api.factory.enchantment;

import com.cubecode.api.scripts.Script;
import com.cubecode.utils.TextUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CubeEnchantment extends Enchantment {
    private final int minLevel;
    private final int maxLevel;
    private final Script onTargetDamaged;
    private final Script onUserDamaged;
    private final String name;

    public CubeEnchantment(Rarity rarity, EnchantmentTarget target, EquipmentSlot[] slotTypes, int minLevel, int maxLevel, Script onTargetDamaged, Script onUserDamaged, String name) {
        super(rarity, target, slotTypes);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.onTargetDamaged = onTargetDamaged;
        this.onUserDamaged = onUserDamaged;
        this.name = name;
    }

    @Override
    public Text getName(int level) {
        MutableText mutableText = TextUtils.formatText(this.name);
        if (this.isCursed()) {
            mutableText.formatted(Formatting.RED);
        } else {
            mutableText.formatted(Formatting.GRAY);
        }

        if (level != 1 || this.getMaxLevel() != 1) {
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + level));
        }

        return mutableText;
    }

    @Override
    public int getMinLevel() {
        return this.minLevel;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        //TODO this.onTargetDamaged.execute();
        super.onTargetDamaged(user, target, level);
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        //TODO this.onUserDamaged.execute();
        super.onUserDamaged(user, attacker, level);
    }
}
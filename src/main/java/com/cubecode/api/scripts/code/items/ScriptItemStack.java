package com.cubecode.api.scripts.code.items;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.cubecode.api.scripts.code.nbt.ScriptNbtList;

public class ScriptItemStack {
    public static final ScriptItemStack EMPTY = new ScriptItemStack(ItemStack.EMPTY);

    private ItemStack stack;
    private ScriptItem item;

    public static ScriptItemStack create(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return EMPTY;
        }

        return new ScriptItemStack(stack);
    }

    private ScriptItemStack(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getMinecraftItemStack() {
        return this.stack;
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public ScriptItemStack copy() {
        return new ScriptItemStack(this.getMinecraftItemStack().copy());
    }

    public ScriptItem getItem() {
        if (this.item == null) {
            this.item = new ScriptItem(this.stack.getItem());
        }

        return this.item;
    }

    public int getMaxCount() {
        return this.stack.getMaxCount();
    }

    public int getCount() {
        return this.stack.getCount();
    }

    public void setCount(int count) {
        this.stack.setCount(count);
    }

    public void addEnchantment(String enchantment, int level) {
        this.stack.addEnchantment(Registries.ENCHANTMENT.get(new Identifier(enchantment)), level);
    }

    public ScriptNbtList getEnchantments() {
        return new ScriptNbtList(this.stack.getEnchantments());
    }

    public boolean isEnchantable() {
        return this.stack.isEnchantable();
    }

    public String getId() {
        return this.stack.getItem().getTranslationKey();
    }
}

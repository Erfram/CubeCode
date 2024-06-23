package com.cubecode.api.scripts.code.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ScriptItem {
    private Item item;

    public ScriptItem(Item item) {
        this.item = item;
    }

    public ItemStack getItemStack() {
        return this.item.getDefaultStack();
    }

    public Item getMinecraftItem() {
        return this.item;
    }
}
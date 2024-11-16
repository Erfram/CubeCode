package com.cubecode.api.scripts.code.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ScriptItem {
    private Item item;

    public ScriptItem(Item item) {
        this.item = item;
    }

    /**
     * Returns the Minecraft ItemStack associated with this script item stack
     * <pre>{@code
     * c.getPlayer().getMainItemStack().getItem().getItemStack();
     * }</pre>
     */
    public ItemStack getItemStack() {
        return this.item.getDefaultStack();
    }

    /**
     * Returns the Minecraft Item associated with this script item
     *
     * <pre>{@code
     * c.getPlayer().getMainItemStack().getItem().getMinecraftItem();
     * }</pre>
     */
    public Item getMinecraftItem() {
        return this.item;
    }
}
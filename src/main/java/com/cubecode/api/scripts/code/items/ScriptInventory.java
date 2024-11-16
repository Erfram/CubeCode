package com.cubecode.api.scripts.code.items;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * c.getPlayer().getInventory().clear();
 */
public class ScriptInventory {
    private Inventory inventory;

    public ScriptInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Returns the Minecraft inventory associated with this script inventory
     */
    public Inventory getMinecraftInventory() {
        return this.inventory;
    }

    /**
     * Checks if the inventory is empty
     */
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    /**
     * Returns the size of the inventory
     */
    public int size() {
        return this.inventory.size();
    }

    /**
     * Returns the item stack at the specified index in the inventory
     *
     * <pre>{@code
     * c.getPlayer().getInventory().getStack(0);
     * }</pre>
     */
    public ScriptItemStack getStack(int index) {
        return ScriptItemStack.create(this.inventory.getStack(index));
    }

    /**
     * Removes and returns the item stack at the specified index in the inventory
     */
    public ScriptItemStack removeStack(int index) {
        return ScriptItemStack.create(this.inventory.removeStack(index));
    }

    /**
     * Sets the item stack at the specified index in the inventory
     *
     * <pre>{@code
     * c.getPlayer().getInventory().setStack(0, c.getPlayer().getMainItemStack());
     * }</pre>
     */
    public void setStack(int index, ScriptItemStack stack) {
        if (stack == null) {
            stack = ScriptItemStack.EMPTY;
        }

        if (index >= 0 && index < this.size()) {
            this.inventory.setStack(index, stack.getMinecraftItemStack());
        }
    }

    /**
     * Clears all items from the inventory
     */
    public void clear() {
        for (int i = 0; i < this.size(); i++) {
            this.inventory.setStack(i, ItemStack.EMPTY);
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getName() {
        return this.inventory.toString();
    }
}

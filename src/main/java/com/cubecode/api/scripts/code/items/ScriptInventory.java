package com.cubecode.api.scripts.code.items;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ScriptInventory {
    private Inventory inventory;

    public ScriptInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getMinecraftInventory() {
        return this.inventory;
    }

    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    public int size() {
        return this.inventory.size();
    }

    public ScriptItemStack getStack(int index) {
        return ScriptItemStack.create(this.inventory.getStack(index));
    }

    public ScriptItemStack removeStack(int index) {
        return ScriptItemStack.create(this.inventory.removeStack(index));
    }

    public void setStack(int index, ScriptItemStack stack) {
        if (stack == null) {
            stack = ScriptItemStack.EMPTY;
        }

        if (index >= 0 && index < this.size()) {
            this.inventory.setStack(index, stack.getMinecraftItemStack());
        }
    }

    public void clear() {
        for (int i = 0; i < this.size(); i++) {
            this.inventory.setStack(i, ItemStack.EMPTY);
        }
    }

    public String getName() {
        return this.inventory.toString();
    }
}

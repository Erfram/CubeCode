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

    /**
     * Returns the Minecraft ItemStack associated with this script item stack
     */
    public ItemStack getMinecraftItemStack() {
        return this.stack;
    }

    /**
     * Checks if the item stack is empty
     */
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    /**
     * Creates and returns a copy of this item stack
     */
    public ScriptItemStack copy() {
        return new ScriptItemStack(this.getMinecraftItemStack().copy());
    }

    /**
     * Returns the ScriptItem associated with this item stack
     */
    public ScriptItem getItem() {
        if (this.item == null) {
            this.item = new ScriptItem(this.stack.getItem());
        }

        return this.item;
    }

    /**
     * Returns the maximum stack size for this item
     */
    public int getMaxCount() {
        return this.stack.getMaxCount();
    }

    /**
     * Returns the current count of items in this stack
     */
    public int getCount() {
        return this.stack.getCount();
    }

    /**
     * Sets the count of items in this stack
     */
    public void setCount(int count) {
        this.stack.setCount(count);
    }

    /**
     * Adds an enchantment to the item stack
     *
     * <pre>{@code
     * c.getPlayer().getMainItemStack().addEnchantment("mending", 0);
     * }</pre>
     */
    public void addEnchantment(String enchantment, int level) {
        this.stack.addEnchantment(Registries.ENCHANTMENT.get(new Identifier(enchantment)), level);
    }

    /**
     * Returns the list of enchantments on the item stack
     */
    public ScriptNbtList getEnchantments() {
        return new ScriptNbtList(this.stack.getEnchantments());
    }

    /**
     * Checks if the item stack can be enchanted
     */
    public boolean isEnchantable() {
        return this.stack.isEnchantable();
    }

    /**
     * Returns the identifier of the item
     */
    public String getId() {
        return this.stack.getItem().getTranslationKey();
    }
}

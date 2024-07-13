package com.cubecode.api.factory.item;

import com.cubecode.utils.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CubeToolItem extends ToolItem {
    String name;
    String description;
    public CubeToolItem(ToolMaterial material, Settings settings, String name, String description) {
        super(material, settings);
        this.name = name;
        this.description = description;
    }

    @Override
    public Text getName() {
        return TextUtils.formatText(this.name);
    }

    @Override
    public Text getName(ItemStack stack) {
        return TextUtils.formatText(this.name);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.set(0, TextUtils.formatText(this.name));
        if (!description.isEmpty()) {
            String[] lines = this.description.split("\n");

            for (String line : lines) {
                tooltip.add(TextUtils.formatText(line));
            }
        }
    }
}

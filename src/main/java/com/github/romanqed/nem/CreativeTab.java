package com.github.romanqed.nem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class CreativeTab extends CreativeTabs {
    private ItemStack stack;

    public CreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return stack;
    }

    public void setTabIconItem(Item item) {
        this.stack = new ItemStack(item);
    }

    public void setTabIconItemStack(ItemStack stack) {
        this.stack = stack;
    }
}

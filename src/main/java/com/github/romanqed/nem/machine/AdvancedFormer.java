package com.github.romanqed.nem.machine;

import ic2.api.recipe.IRecipeInput;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public abstract class AdvancedFormer
        extends AbstractDynamicMachine<IRecipeInput, Collection<ItemStack>, ItemStack> {
    public AdvancedFormer(int energyPerTick, int length, int tier, int slots) {
        super(energyPerTick, length, tier, slots);
    }

    @Override
    protected String getProgressStyle() {
        return "progressarrow";
    }

    @Override
    protected Collection<ItemStack> getOutput(Collection<ItemStack> output) {
        return StackUtil.copy(output);
    }

    @Override
    protected String getStartSoundFile() {
        return null;
    }

    @Override
    protected String getInterruptSoundFile() {
        return null;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return Constants.PROPERTIES;
    }
}

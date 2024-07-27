package com.github.romanqed.nem.machine;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public class AdvancedCompressor extends AbstractDynamicMachine<IRecipeInput, Collection<ItemStack>, ItemStack> {

    public AdvancedCompressor(int energyPerTick, int length, int aDefaultTier, int slots) {
        super(energyPerTick, length, aDefaultTier, slots);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected IMachineRecipeManager getRecipeManager() {
        return Recipes.compressor;
    }

    @Override
    protected String getRecipeEvent() {
        return "compressor";
    }

    @Override
    protected String getProgressStyle() {
        return "progresstriangle";
    }

    @Override
    protected Collection<ItemStack> getOutput(Collection<ItemStack> output) {
        return StackUtil.copy(output);
    }

    @Override
    protected String getStartSoundFile() {
        return "Machines/CompressorOp.ogg";
    }

    @Override
    protected String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return Constants.PROPERTIES;
    }
}

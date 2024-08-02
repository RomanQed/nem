package com.github.romanqed.nem.machine;

import com.github.romanqed.nem.config.NemConfig;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public class AdvancedRecycler extends AbstractDynamicMachine<IRecipeInput, Collection<ItemStack>, ItemStack> {

    public AdvancedRecycler(int energyPerTick, int length, int aDefaultTier, int slots) {
        super(energyPerTick, length, aDefaultTier, slots);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected IMachineRecipeManager getRecipeManager() {
        return Recipes.recycler;
    }

    @Override
    protected String getRecipeEvent() {
        return "recycler";
    }

    @Override
    protected String getProgressStyle() {
        return "progressrecycler";
    }

    @Override
    protected Collection<ItemStack> getOutput(Collection<ItemStack> output) {
        return StackUtil.copy(output);
    }

    @Override
    protected void operate(InvSlotProcessable<IRecipeInput, Collection<ItemStack>, ItemStack> input,
                           InvSlotOutput output,
                           MachineRecipeResult<IRecipeInput, Collection<ItemStack>, ItemStack> result,
                           Collection<ItemStack> processResult) {
        input.consume(result);
        if (IC2.random.nextInt(NemConfig.recycleChance) == 0) {
            output.add(processResult);
        }
    }

    @Override
    protected String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    @Override
    protected String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return Constants.MACHINE_PROPERTIES;
    }
}

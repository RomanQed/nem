package com.github.romanqed.nem.machine;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class AdvancedMacerator extends AbstractDynamicMachine<IRecipeInput, Collection<ItemStack>, ItemStack> {

    public AdvancedMacerator(int energyPerTick, int length, int aDefaultTier, int slots) {
        super(energyPerTick, length, aDefaultTier, slots);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected IMachineRecipeManager getRecipeManager() {
        return Recipes.macerator;
    }

    @Override
    protected String getRecipeEvent() {
        return "macerator";
    }

    @Override
    protected String getProgressStyle() {
        return "progresscrush";
    }

    @Override
    protected Collection<ItemStack> getOutput(Collection<ItemStack> output) {
        return StackUtil.copy(output);
    }

    private void spawnParticle(World world, Random random) {
        double x = (double) this.pos.getX() + 0.5 + (double) random.nextFloat() * 0.6 - 0.3;
        double y = (double) (this.pos.getY() + 1) + (double) random.nextFloat() * 0.2 - 0.1;
        double z = (double) this.pos.getZ() + 0.5 + (double) random.nextFloat() * 0.6 - 0.3;
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void updateEntityClient() {
        super.updateEntityClient();
        World world = getWorld();
        if (getActive() && world.rand.nextInt(8) == 0) {
            // Manually vectorized cycle
            spawnParticle(world, world.rand);
            spawnParticle(world, world.rand);
            spawnParticle(world, world.rand);
            spawnParticle(world, world.rand);
        }
    }

    @Override
    protected String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
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

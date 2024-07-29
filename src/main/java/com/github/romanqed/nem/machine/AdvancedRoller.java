package com.github.romanqed.nem.machine;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.Recipes;

public class AdvancedRoller extends AdvancedFormer {

    public AdvancedRoller(int energyPerTick, int length, int tier, int slots) {
        super(energyPerTick, length, tier, slots);
    }

    @Override
    protected String getRecipeEvent() {
        return "metal_former1";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected IMachineRecipeManager getRecipeManager() {
        return Recipes.metalformerRolling;
    }
}

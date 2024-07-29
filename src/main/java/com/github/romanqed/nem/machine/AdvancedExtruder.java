package com.github.romanqed.nem.machine;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.Recipes;

public class AdvancedExtruder extends AdvancedFormer {

    public AdvancedExtruder(int energyPerTick, int length, int tier, int slots) {
        super(energyPerTick, length, tier, slots);
    }

    @Override
    protected String getRecipeEvent() {
        return "metal_former0";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected IMachineRecipeManager getRecipeManager() {
        return Recipes.metalformerExtruding;
    }
}

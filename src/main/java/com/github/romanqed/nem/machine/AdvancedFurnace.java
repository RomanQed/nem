package com.github.romanqed.nem.machine;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.audio.AudioManager;
import ic2.core.audio.FutureSound;
import ic2.core.audio.PositionSpec;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class AdvancedFurnace extends AbstractDynamicMachine<ItemStack, ItemStack, ItemStack> {
    private static final String STARTING_SOUND_FILE = "Machines/Electro Furnace/ElectroFurnaceStart.ogg";
    private static final String START_SOUND_FILE = "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    private static final String INTERRUPT_SOUND_FILE = "Machines/Electro Furnace/ElectroFurnaceStop.ogg";

    protected FutureSound startingSound;
    protected String finishingSound;

    public AdvancedFurnace(int energyPerTick, int length, int aDefaultTier, int slots) {
        super(energyPerTick, length, aDefaultTier, slots);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected IMachineRecipeManager getRecipeManager() {
        return Recipes.furnace;
    }

    @Override
    protected String getRecipeEvent() {
        return "minecraft.smelting";
    }

    @Override
    protected String getProgressStyle() {
        return "progressarrow";
    }

    @Override
    protected void initAudioSource() {
        if (audioSource != null) {
            return;
        }
        AudioManager manager = IC2.audioManager;
        this.audioSource = manager.createSource(
                this,
                PositionSpec.Center,
                START_SOUND_FILE,
                true,
                false,
                manager.getDefaultVolume()
        );
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isSimulating()) {
            return;
        }
        if (this.startingSound != null) {
            if (!this.startingSound.isComplete()) {
                this.startingSound.cancel();
            }
            this.startingSound = null;
        }
        if (this.finishingSound != null) {
            IC2.audioManager.removeSource(this.finishingSound);
            this.finishingSound = null;
        }
    }

    @Override
    protected Collection<ItemStack> getOutput(ItemStack output) {
        return Collections.singletonList(output);
    }

    public String getStartingSoundFile() {
        return STARTING_SOUND_FILE;
    }

    @Override
    protected String getStartSoundFile() {
        return START_SOUND_FILE;
    }

    @Override
    protected String getInterruptSoundFile() {
        return INTERRUPT_SOUND_FILE;
    }

    private void handleStart() {
        if (this.startingSound != null) {
            return;
        }
        AudioManager manager = IC2.audioManager;
        if (this.finishingSound != null) {
            manager.removeSource(this.finishingSound);
            this.finishingSound = null;
        }
        String source = manager.playOnce(
                this,
                PositionSpec.Center,
                STARTING_SOUND_FILE,
                false,
                manager.getDefaultVolume()
        );
        if (this.audioSource != null) {
            startingSound = new FutureSound(audioSource::play);
            manager.chainSource(source, startingSound);
        }
    }

    private void handleStop() {
        if (this.audioSource == null) {
            return;
        }
        this.audioSource.stop();
        if (this.startingSound != null) {
            if (!this.startingSound.isComplete()) {
                this.startingSound.cancel();
            }
            this.startingSound = null;
        }
        AudioManager manager = IC2.audioManager;
        this.finishingSound = manager.playOnce(
                this,
                PositionSpec.Center,
                this.getInterruptSoundFile(),
                false,
                manager.getDefaultVolume()
        );
    }

    public void onNetworkEvent(int event) {
        initAudioSource();
        switch (event) {
            case EventStart:
                handleStart();
                break;
            case EventInterrupt:
            case EventStop:
                handleStop();
        }

    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return Constants.MACHINE_PROPERTIES;
    }
}

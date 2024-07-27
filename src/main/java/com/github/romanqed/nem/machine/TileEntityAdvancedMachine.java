package com.github.romanqed.nem.machine;

import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioManager;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import ic2.core.network.NetworkManager;
import ic2.core.util.StackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TileEntityAdvancedMachine<RI, RO, I>
        extends TileEntityElectricMachine
        implements IHasGui, IGuiValueProvider, INetworkTileEntityEventListener, IUpgradableBlock {
    protected static final NetworkManager NETWORK_MANAGER = IC2.network.get(true);

    protected static final int EventStart = 0;
    protected static final int EventInterrupt = 1;
    protected static final int EventFinish = 2;
    protected static final int EventStop = 3;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public final InvSlotUpgrade upgradeSlot;
    // Slots
    protected final List<InvSlotProcessable<RI, RO, I>> inputSlots;
    protected final List<InvSlotOutput> outputSlots;
    protected int energyConsume;
    protected int operationLength;
    protected int operationsPerTick;
    protected AudioSource audioSource;
    protected short progress;
    @GuiSynced
    protected float guiProgress;

    public TileEntityAdvancedMachine(int energyPerTick, int length, int aDefaultTier) {
        super(energyPerTick * length, aDefaultTier);
        this.inputSlots = new ArrayList<>();
        this.outputSlots = new ArrayList<>();
        this.progress = 0;
        this.defaultEnergyConsume = energyPerTick;
        this.energyConsume = energyPerTick;
        this.defaultOperationLength = length;
        this.operationLength = length;
        this.defaultTier = aDefaultTier;
        this.defaultEnergyStorage = energyPerTick * length;
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.comparator.setUpdate(() -> this.progress * 15 / this.operationLength);
        initAudioSource();
    }

    protected void initAudioSource() {
        if (audioSource != null) {
            return;
        }
        String file = getStartSoundFile();
        if (file == null) {
            return;
        }
        this.audioSource = IC2.audioManager.createSource(this, file);
    }

    protected void addSlotPair(InvSlotProcessable<RI, RO, I> input, InvSlotOutput output) {
        inputSlots.add(input);
        outputSlots.add(output);
    }

    public float getProgress() {
        return this.guiProgress;
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        double previousProgress = (double) progress / (double) operationLength;
        this.operationsPerTick = upgradeSlot.getOperationsPerTick(defaultOperationLength);
        this.operationLength = upgradeSlot.getOperationLength(defaultOperationLength);
        this.energyConsume = upgradeSlot.getEnergyDemand(defaultEnergyConsume);
        int tier = upgradeSlot.getTier(defaultTier);
        this.energy.setSinkTier(tier);
        this.dischargeSlot.setTier(tier);
        this.energy.setCapacity(
                upgradeSlot.getEnergyStorage(defaultEnergyStorage, defaultOperationLength, defaultEnergyConsume)
        );
        this.progress = (short) (previousProgress * (double) operationLength + 0.1);
    }

    // Server entity processing
    protected abstract Collection<ItemStack> getOutput(RO output);

    protected MachineRecipeResult<RI, RO, I> getOutput(InvSlotProcessable<RI, RO, I> input, InvSlotOutput output) {
        if (input.isEmpty()) {
            return null;
        }
        MachineRecipeResult<RI, RO, I> ret = input.process();
        if (ret == null) {
            return null;
        }
        return output.canAdd(getOutput(ret.getOutput())) ? ret : null;
    }

    private void interruptWork() {
        if (!getActive()) {
            return;
        }
        if (this.progress != 0) {
            NETWORK_MANAGER.initiateTileEntityEvent(this, EventInterrupt, true);
        } else {
            NETWORK_MANAGER.initiateTileEntityEvent(this, EventStop, true);
        }
    }

    private void updateGuiProgress() {
        this.guiProgress = (float) this.progress / (float) this.operationLength;
    }

    protected void operate(InvSlotProcessable<RI, RO, I> input,
                           InvSlotOutput output,
                           MachineRecipeResult<RI, RO, I> result,
                           Collection<ItemStack> processResult) {
        input.consume(result);
        output.add(processResult);
    }

    private void operate(InvSlotProcessable<RI, RO, I> input,
                         InvSlotOutput output,
                         MachineRecipeResult<RI, RO, I> result) {
        int size = upgradeSlot.size();
        for (int i = 0; i < this.operationsPerTick; ++i) {
            Collection<ItemStack> processResult = getOutput(result.getOutput());
            for (int j = 0; j < size; ++j) {
                ItemStack stack = upgradeSlot.get(j);
                if (StackUtil.isEmpty(stack)) {
                    continue;
                }
                Item item = stack.getItem();
                if (item instanceof IUpgradeItem) {
                    processResult = ((IUpgradeItem) item).onProcessEnd(stack, this, processResult);
                }
            }
            operate(input, output, result, processResult);
            if (getOutput(input, output) == null) {
                break;
            }
        }
    }

    private void operate(MachineRecipeResult<RI, RO, I>[] results) {
        int size = results.length;
        for (int i = 0; i < size; ++i) {
            MachineRecipeResult<RI, RO, I> result = results[i];
            if (result == null) {
                continue;
            }
            InvSlotProcessable<RI, RO, I> input = inputSlots.get(i);
            InvSlotOutput output = outputSlots.get(i);
            operate(input, output, result);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void updateEntityServer() {
        super.updateEntityServer();
        // If there is no energy, just stop work
        if (!energy.canUseEnergy(energyConsume)) {
            interruptWork();
            setActive(false);
            if (upgradeSlot.tickNoMark()) {
                markDirty();
            }
            return;
        }
        // Check if machine can process input slots
        int size = inputSlots.size();
        boolean hasOutput = false;
        MachineRecipeResult<RI, RO, I>[] outputs = new MachineRecipeResult[size];
        for (int i = 0; i < size; ++i) {
            InvSlotProcessable<RI, RO, I> input = inputSlots.get(i);
            InvSlotOutput output = outputSlots.get(i);
            MachineRecipeResult<RI, RO, I> result = getOutput(input, output);
            outputs[i] = result;
            hasOutput |= result != null;
        }
        // If not, interrupt processing
        if (!hasOutput) {
            interruptWork();
            progress = 0;
            setActive(false);
            if (upgradeSlot.tickNoMark()) {
                markDirty();
            }
            updateGuiProgress();
            return;
        }
        // Otherwise do processing
        energy.useEnergy(energyConsume);
        setActive(true);
        if (progress == 0) {
            NETWORK_MANAGER.initiateTileEntityEvent(this, EventStart, true);
        }
        ++progress;
        boolean needsInvUpdate = false;
        if (progress >= operationLength) {
            operate(outputs);
            needsInvUpdate = true;
            progress = 0;
            NETWORK_MANAGER.initiateTileEntityEvent(this, EventFinish, true);
        }
        needsInvUpdate |= this.upgradeSlot.tickNoMark();
        updateGuiProgress();
        if (needsInvUpdate) {
            markDirty();
        }
    }

    // Load hooks
    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    // Inv hook
    @Override
    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }
    }

    // NBT
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort("progress", this.progress);
        return nbt;
    }

    protected abstract String getStartSoundFile();

    protected abstract String getInterruptSoundFile();

    // Networking
    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null) {
            return;
        }
        switch (event) {
            case EventStart:
                this.audioSource.play();
                return;
            case EventInterrupt:
                this.audioSource.stop();
                if (this.getInterruptSoundFile() != null) {
                    AudioManager manager = IC2.audioManager;
                    manager.playOnce(
                            this,
                            PositionSpec.Center,
                            this.getInterruptSoundFile(),
                            false,
                            manager.getDefaultVolume()
                    );
                }
                return;
            case EventFinish:
                this.audioSource.stop();
        }
    }

    @Override
    public double getEnergy() {
        return this.energy.getEnergy();
    }

    @Override
    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    // GUI
    @Override
    public double getGuiValue(String name) {
        if ("progress".equals(name)) {
            return this.guiProgress;
        }
        throw new IllegalArgumentException(this.getClass().getSimpleName() + " Cannot get value for " + name);
    }
}

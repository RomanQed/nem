package com.github.romanqed.nem.machine;

import com.github.romanqed.nem.gui.AdvancedMachineGuiBuilder;
import com.github.romanqed.nem.gui.GuiBuilder;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.core.ContainerBase;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractDynamicMachine<RI, RO, I> extends TileEntityAdvancedMachine<RI, RO, I> {
    private static final GuiBuilder BUILDER = new AdvancedMachineGuiBuilder();
    private static final String INPUT = "input";
    private static final String OUTPUT = "output";

    protected final GuiParser.GuiNode gui;

    public AbstractDynamicMachine(int energyPerTick,
                                  int length,
                                  int aDefaultTier,
                                  int slots) {
        super(energyPerTick, length, aDefaultTier);
        this.generateSlots(slots);
        this.gui = generateGui(slots);
    }

    @SuppressWarnings("rawtypes")
    protected abstract IMachineRecipeManager getRecipeManager();

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void generateSlots(int slots) {
        IMachineRecipeManager manager = getRecipeManager();
        for (int i = 0; i < slots; ++i) {
            InvSlotProcessable input = new InvSlotProcessableGeneric(
                    this,
                    INPUT + i,
                    1,
                    manager
            );
            InvSlotOutput output = new InvSlotOutput(this, OUTPUT + i, 1);
            addSlotPair(input, output);
        }
    }

    protected String getRecipeEvent() {
        return null;
    }

    protected abstract String getProgressStyle();

    private GuiParser.GuiNode generateGui(int slots) {
        BUILDER.setBaseClass(this.getClass());
        BUILDER.setProgress("progress", getProgressStyle(), getRecipeEvent());
        for (int i = 0; i < slots; ++i) {
            BUILDER.addInputSlot(INPUT + i);
            BUILDER.addOutputSlot(OUTPUT + i);
        }
        return BUILDER.build();
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, gui);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return DynamicGui.create(this, player, gui);
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }
}

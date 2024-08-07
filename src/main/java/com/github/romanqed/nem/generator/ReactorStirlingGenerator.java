package com.github.romanqed.nem.generator;

import com.github.romanqed.nem.Upgrades;
import com.github.romanqed.nem.gui.ContainerReactorStirlingGenerator;
import com.github.romanqed.nem.gui.GuiReactorStirlingGenerator;
import ic2.api.energy.tile.IHeatSource;
import ic2.api.energy.tile.IKineticSource;
import ic2.api.recipe.ILiquidAcceptManager;
import ic2.api.recipe.ILiquidHeatExchangerManager.HeatExchangeProperty;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Fluids;
import ic2.core.block.invslot.InvSlot.Access;
import ic2.core.block.invslot.InvSlot.InvSide;
import ic2.core.block.invslot.InvSlotConsumableLiquid.OpType;
import ic2.core.block.invslot.InvSlotConsumableLiquidByManager;
import ic2.core.block.invslot.InvSlotConsumableLiquidByTank;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.block.machine.tileentity.TileEntityLiquidHeatExchanger;
import ic2.core.profile.NotClassic;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@NotClassic
public class ReactorStirlingGenerator extends TileEntityInventory implements IKineticSource, IUpgradableBlock, IHasGui {
    protected final Fluids fluids = this.addComponent(new Fluids(this));
    private final int maxHeatBuffer;
    private final int maxKUBuffer;
    public FluidTank inputTank;
    public FluidTank outputTank;
    public InvSlotOutput hotOutputSlot;
    public InvSlotOutput coolOutputSlot;
    public InvSlotConsumableLiquidByTank hotFluidInputSlot;
    public InvSlotConsumableLiquidByManager coolFluidInputSlot;
    public InvSlotUpgrade upgradeSlot;
    private int heatBuffer = 0;
    private int kuBuffer;
    private int liquidHeatStored;

    public ReactorStirlingGenerator() {
        this.inputTank = this.fluids.addTankInsert(
                "inputTank",
                2000,
                Fluids.fluidPredicate(Recipes.liquidHeatupManager.getSingleDirectionLiquidManager())
        );
        this.outputTank = this.fluids.addTankExtract("outputTank", 2000);
        this.hotOutputSlot = new InvSlotOutput(this, "hotOutputSlot", 1);
        this.coolOutputSlot = new InvSlotOutput(this, "outputSlot", 1);
        this.coolFluidInputSlot = new InvSlotConsumableLiquidByManager(
                this,
                "coolFluidInputSlot",
                Access.I,
                1,
                InvSide.TOP,
                OpType.Drain,
                Recipes.liquidHeatupManager.getSingleDirectionLiquidManager()
        );
        this.hotFluidInputSlot = new InvSlotConsumableLiquidByTank(
                this,
                "hotFluidOutputSlot",
                Access.I,
                1,
                InvSide.BOTTOM,
                OpType.Fill,
                this.outputTank
        );
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 3);
        this.maxHeatBuffer = 1000;
        this.maxKUBuffer = 2000;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.inputTank.readFromNBT(nbt.getCompoundTag("inputTank"));
        this.outputTank.readFromNBT(nbt.getCompoundTag("outputTank"));
        this.heatBuffer = nbt.getInteger("heatBuffer");
        this.kuBuffer = nbt.getInteger("kuBuffer");
        this.liquidHeatStored = nbt.getInteger("liquidHeatStored");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound inputTankTag = new NBTTagCompound();
        this.inputTank.writeToNBT(inputTankTag);
        nbt.setTag("inputTank", inputTankTag);
        NBTTagCompound outputTankTag = new NBTTagCompound();
        this.outputTank.writeToNBT(outputTankTag);
        nbt.setTag("outputTank", outputTankTag);
        nbt.setInteger("heatBuffer", this.heatBuffer);
        nbt.setInteger("kuBuffer", this.kuBuffer);
        nbt.setInteger("liquidHeatStored", this.liquidHeatStored);
        return nbt;
    }

    protected boolean innerUpdateEntityServer() {
        int inputFluid = inputTank.getFluidAmount();
        int outputFluid = outputTank.getFluidAmount();
        int outputCapacity = outputTank.getCapacity();
        if (inputFluid == 0 || outputFluid >= outputCapacity) {
            return false;
        }
        FluidStack fs = inputTank.getFluid();
        if (fs == null) {
            return false;
        }
        Fluid fluid = fs.getFluid();
        ILiquidAcceptManager manager = Recipes.liquidHeatupManager.getSingleDirectionLiquidManager();
        if (!manager.acceptsFluid(fluid)) {
            return false;
        }
        if (this.kuBuffer >= this.maxKUBuffer) {
            return false;
        }
        HeatExchangeProperty property = Recipes.liquidHeatupManager.getHeatExchangeProperty(fluid);
        FluidStack ofs = outputTank.getFluid();
        if (ofs != null && ofs.getFluid() != property.outputFluid) {
            return false;
        }
        int heatbufferToUse = heatBuffer / 4;
        heatbufferToUse = Math.min(
                heatbufferToUse,
                Math.min(outputCapacity - outputFluid, inputFluid) * property.huPerMB - this.liquidHeatStored
        );
        heatbufferToUse = Math.min(heatbufferToUse, (this.maxKUBuffer - this.kuBuffer) / 3);
        boolean ret = false;
        if (heatbufferToUse > 0) {
            this.kuBuffer += heatbufferToUse * 3 * 4;
            this.liquidHeatStored += heatbufferToUse;
            this.heatBuffer -= heatbufferToUse * 4;
            ret = true;
        }
        if (this.liquidHeatStored >= property.huPerMB) {
            int mbToConvert = this.liquidHeatStored / property.huPerMB;
            FluidStack drained = this.inputTank.drainInternal(mbToConvert, false);
            if (drained == null) {
                return ret;
            }
            mbToConvert = drained.amount;
            mbToConvert = outputTank.fillInternal(new FluidStack(property.outputFluid, mbToConvert), false);
            this.liquidHeatStored -= mbToConvert * property.huPerMB;
            this.inputTank.drainInternal(mbToConvert, true);
            this.outputTank.fillInternal(new FluidStack(property.outputFluid, mbToConvert), true);
        }
        return ret;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.coolFluidInputSlot.processIntoTank(this.inputTank, this.coolOutputSlot);
        this.hotFluidInputSlot.processFromTank(this.outputTank, this.hotOutputSlot);
        if (this.heatBuffer < this.maxHeatBuffer) {
            this.heatBuffer += this.drawHu(this.maxHeatBuffer - this.heatBuffer);
        }
        boolean active = innerUpdateEntityServer();
        if (getActive() != active) {
            setActive(active);
        }
        this.upgradeSlot.tick();
    }

    private int drawHu(int amount) {
        if (amount <= 0) {
            return 0;
        }
        World world = this.getWorld();
        int tmpAmount = amount;
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir == this.getFacing()) {
                continue;
            }
            TileEntity te = world.getTileEntity(this.pos.offset(dir));
            if (te == null || te.getClass() != TileEntityLiquidHeatExchanger.class) {
                continue;
            }
            IHeatSource hs = (IHeatSource) te;
            int request = hs.drawHeat(dir.getOpposite(), tmpAmount, true);
            if (request > 0) {
                tmpAmount -= hs.drawHeat(dir.getOpposite(), request, false);
                if (tmpAmount <= 0) {
                    break;
                }
            }
        }

        return amount - tmpAmount;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int maxrequestkineticenergyTick(EnumFacing directionFrom) {
        return Math.min(kuBuffer, getConnectionBandwidth(directionFrom));
    }

    @Override
    public int getConnectionBandwidth(EnumFacing side) {
        return side != this.getFacing() ? 0 : maxKUBuffer;
    }

    @Override
    public int requestkineticenergy(EnumFacing directionFrom, int requestKineticEnergy) {
        return this.drawKineticEnergy(directionFrom, requestKineticEnergy, false);
    }

    @Override
    public int drawKineticEnergy(EnumFacing side, int request, boolean simulate) {
        if (side != this.getFacing()) {
            return 0;
        }
        if (request > kuBuffer) {
            request = kuBuffer;
        }

        if (!simulate) {
            kuBuffer -= request;
        }

        return request;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return Upgrades.STIRLING_PROPERTIES;
    }

    @Override
    public double getEnergy() {
        return 40.0;
    }

    @Override
    public boolean useEnergy(double amount) {
        return true;
    }

    public FluidTank getInputTank() {
        return this.inputTank;
    }

    public FluidTank getOutputTank() {
        return this.outputTank;
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerReactorStirlingGenerator(player, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiReactorStirlingGenerator(new ContainerReactorStirlingGenerator(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {
    }
}

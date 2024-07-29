package com.github.romanqed.nem.generator;

import ic2.api.energy.tile.IKineticSource;
import ic2.core.block.generator.tileentity.TileEntityKineticGenerator;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityAdvancedKineticGenerator extends TileEntityKineticGenerator {
    public static final double BASE_EU_PER_KU =
            ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/Kinetic");

    private final double euPerKu;
    private double effective;

    public TileEntityAdvancedKineticGenerator(double multiplier) {
        this.euPerKu = multiplier * BASE_EU_PER_KU;
        this.effective = 1;
    }

    @Override
    protected double getMultiplier() {
        return this.effective * this.euPerKu;
    }

    protected abstract double updateEffective(IKineticSource source);

    @Override
    protected void updateSource() {
        if (this.source != null && !((TileEntity) this.source).isInvalid()) {
            return;
        }
        TileEntity entity = this.world.getTileEntity(this.pos.offset(this.getFacing()));
        if ((!(entity instanceof IKineticSource))) {
            this.source = null;
            return;
        }
        IKineticSource source = (IKineticSource) entity;
        this.source = source;
        this.effective = updateEffective(source);
    }
}

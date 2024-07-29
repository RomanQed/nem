package com.github.romanqed.nem.generator;

import ic2.api.energy.tile.IKineticSource;
import ic2.core.block.kineticgenerator.tileentity.TileEntityStirlingKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWaterKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;

public class CommonKineticGenerator extends TileEntityAdvancedKineticGenerator {
    private final double naturalEffective;
    private final double reactorEffective;

    public CommonKineticGenerator(double multiplier, double naturalEffective, double reactorEffective) {
        super(multiplier);
        this.naturalEffective = naturalEffective;
        this.reactorEffective = reactorEffective;
    }

    @Override
    protected double updateEffective(IKineticSource source) {
        Class<?> clazz = source.getClass();
        if (clazz == TileEntityWaterKineticGenerator.class || clazz == TileEntityWindKineticGenerator.class) {
            return naturalEffective;
        }
        if (clazz == TileEntityStirlingKineticGenerator.class) {
            return reactorEffective;
        }
        return 0.01;
    }
}

package com.github.romanqed.nem.generator;

import com.github.romanqed.nem.config.NemConfig;
import ic2.api.energy.tile.IKineticSource;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWaterKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;

public class ReactorKineticGenerator extends TileEntityAdvancedKineticGenerator {
    private final double naturalEffective;
    private final double reactorEffective;

    public ReactorKineticGenerator() {
        super(NemConfig.reactorKineticMultiplier);
        this.naturalEffective = NemConfig.naturalEffective;
        this.reactorEffective = NemConfig.reactorEffective;
    }

    @Override
    protected double updateEffective(IKineticSource source) {
        Class<?> clazz = source.getClass();
        if (clazz == TileEntityWaterKineticGenerator.class || clazz == TileEntityWindKineticGenerator.class) {
            return naturalEffective;
        }
        if (clazz == ReactorStirlingGenerator.class) {
            return reactorEffective;
        }
        return 0.01;
    }
}

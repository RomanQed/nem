package com.github.romanqed.nem.generator;

import com.github.romanqed.nem.config.NemConfig;

public final class SuperKineticGenerator extends CommonKineticGenerator {

    public SuperKineticGenerator() {
        super(NemConfig.superKineticMultiplier, NemConfig.naturalEffective, NemConfig.reactorEffective);
    }
}

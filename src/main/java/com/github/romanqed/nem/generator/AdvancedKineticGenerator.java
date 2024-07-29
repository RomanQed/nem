package com.github.romanqed.nem.generator;

import com.github.romanqed.nem.config.NemConfig;

public final class AdvancedKineticGenerator extends CommonKineticGenerator {

    public AdvancedKineticGenerator() {
        super(NemConfig.advancedKineticMultiplier, NemConfig.naturalEffective, NemConfig.reactorEffective);
    }
}

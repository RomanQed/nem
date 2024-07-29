package com.github.romanqed.nem.config;

import net.minecraftforge.common.config.Config;

@Config(modid = "nem")
public final class NemConfig {

    @Config.RangeDouble(min = 0.01, max = 100)
    public static double machineEnergyMultiplier = 1;

    @Config.RangeDouble(min = 0.01, max = 100)
    public static double advancedKineticMultiplier = 0.85;

    @Config.RangeDouble(min = 0.01, max = 100)
    public static double superKineticMultiplier = 1.2;

    @Config.RangeDouble(min = 0.01, max = 100)
    public static double reactorEffective = 20;

    @Config.RangeDouble(min = 0.01, max = 100)
    public static double naturalEffective = 0.5;
}

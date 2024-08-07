package com.github.romanqed.nem.config;

import net.minecraftforge.common.config.Config;

@Config(modid = "nem")
public final class NemConfig {

    @Config.RangeDouble(min = 0.01, max = 100)
    @Config.RequiresMcRestart
    public static double machineEnergyMultiplier = 1;

    @Config.RangeDouble(min = 0.01, max = 100)
    @Config.RequiresMcRestart
    public static double reactorKineticMultiplier = 1;

    @Config.RangeDouble(min = 0.01, max = 100)
    @Config.RequiresMcRestart
    public static double reactorEffective = 25;

    @Config.RangeDouble(min = 0.01, max = 100)
    @Config.RequiresMcRestart
    public static double naturalEffective = 0.5;

    @Config.RangeInt(min = 1, max = 100)
    @Config.RequiresMcRestart
    public static int recycleChance = 15;

    @Config.RequiresMcRestart
    public static boolean enableMachines = true;

    @Config.RequiresMcRestart
    public static boolean enableGenerators = true;
}

package com.github.romanqed.nem;

import ic2.api.upgrade.UpgradableProperty;

import java.util.EnumSet;
import java.util.Set;

public final class Upgrades {
    public static final Set<UpgradableProperty> MACHINE_PROPERTIES = EnumSet.of(
            UpgradableProperty.Processing,
            UpgradableProperty.Transformer,
            UpgradableProperty.EnergyStorage,
            UpgradableProperty.ItemConsuming,
            UpgradableProperty.ItemProducing
    );

    public static final Set<UpgradableProperty> STIRLING_PROPERTIES = EnumSet.of(
            UpgradableProperty.ItemConsuming,
            UpgradableProperty.ItemProducing,
            UpgradableProperty.FluidConsuming,
            UpgradableProperty.FluidProducing
    );

    private Upgrades() {
    }
}

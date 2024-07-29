package com.github.romanqed.nem.machine;

import ic2.api.upgrade.UpgradableProperty;

import java.util.EnumSet;
import java.util.Set;

final class Constants {
    public static final Set<UpgradableProperty> PROPERTIES = EnumSet.of(
            UpgradableProperty.Processing,
            UpgradableProperty.Transformer,
            UpgradableProperty.EnergyStorage,
            UpgradableProperty.ItemConsuming,
            UpgradableProperty.ItemProducing
    );

    private Constants() {
    }
}

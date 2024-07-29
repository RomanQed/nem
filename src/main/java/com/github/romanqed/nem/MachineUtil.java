package com.github.romanqed.nem;

import com.github.romanqed.nem.config.NemConfig;
import com.github.romanqed.nem.machine.*;
import com.github.romanqed.nem.util.AdvancedMachineGenerator;
import com.github.romanqed.nem.util.TileClassGenerator;
import ic2.core.block.TileEntityBlock;

public final class MachineUtil {
    public static final int FURNACE_LENGTH = 100;
    public static final int MACERATOR_LENGTH = 300;
    public static final int EXTRACTOR_LENGTH = 300;
    public static final int FORMER_LENGTH = 200;
    public static final int COMPRESSOR_LENGTH = 300;
    public static final int FURNACE_ENERGY = 3;
    public static final int MACERATOR_ENERGY = 2;
    public static final int EXTRACTOR_ENERGY = 2;
    public static final int FORMER_ENERGY = 10;
    public static final int COMPRESSOR_ENERGY = 2;
    private static final TileClassGenerator GENERATOR = new AdvancedMachineGenerator(MachineUtil.class);
    private static final String ADVANCED_FURNACE = "AdvancedFurnace";
    private static final String ADVANCED_MACERATOR = "AdvancedMacerator";
    private static final String ADVANCED_EXTRACTOR = "AdvancedExtractor";
    private static final String ADVANCED_CUTTER = "AdvancedCutter";
    private static final String ADVANCED_EXTRUDER = "AdvancedExtruder";
    private static final String ADVANCED_ROLLER = "AdvancedRoller";
    private static final String ADVANCED_COMPRESSOR = "AdvancedCompressor";

    private MachineUtil() {
    }

    public static int calculateEnergyPerTick(int base, int slots) {
        return (int) (base * slots * NemConfig.machineEnergyMultiplier);
    }

    public static IC2Tier getTier(int slots) {
        if (slots <= 3) {
            return IC2Tier.MEDIUM;
        }
        if (slots <= 6) {
            return IC2Tier.HIGH;
        }
        return IC2Tier.EXTREME;
    }

    public static Class<TileEntityBlock> createFurnace(int slots) {
        int energy = calculateEnergyPerTick(FURNACE_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedFurnace.class,
                ADVANCED_FURNACE + slots,
                new Object[]{energy, FURNACE_LENGTH, tier.getValue(), slots}
        );
    }

    public static Class<TileEntityBlock> createMacerator(int slots) {
        int energy = calculateEnergyPerTick(MACERATOR_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedMacerator.class,
                ADVANCED_MACERATOR + slots,
                new Object[]{energy, MACERATOR_LENGTH, tier.getValue(), slots}
        );
    }

    public static Class<TileEntityBlock> createExtractor(int slots) {
        int energy = calculateEnergyPerTick(EXTRACTOR_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedExtractor.class,
                ADVANCED_EXTRACTOR + slots,
                new Object[]{energy, EXTRACTOR_LENGTH, tier.getValue(), slots}
        );
    }

    public static Class<TileEntityBlock> createCutter(int slots) {
        int energy = calculateEnergyPerTick(FORMER_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedCutter.class,
                ADVANCED_CUTTER + slots,
                new Object[]{energy, FORMER_LENGTH, tier.getValue(), slots}
        );
    }

    public static Class<TileEntityBlock> createExtruder(int slots) {
        int energy = calculateEnergyPerTick(FORMER_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedExtruder.class,
                ADVANCED_EXTRUDER + slots,
                new Object[]{energy, FORMER_LENGTH, tier.getValue(), slots}
        );
    }

    public static Class<TileEntityBlock> createRoller(int slots) {
        int energy = calculateEnergyPerTick(FORMER_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedRoller.class,
                ADVANCED_ROLLER + slots,
                new Object[]{energy, FORMER_LENGTH, tier.getValue(), slots}
        );
    }

    public static Class<TileEntityBlock> createCompressor(int slots) {
        int energy = calculateEnergyPerTick(COMPRESSOR_ENERGY, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(
                AdvancedCompressor.class,
                ADVANCED_COMPRESSOR + slots,
                new Object[]{energy, COMPRESSOR_LENGTH, tier.getValue(), slots}
        );
    }
}

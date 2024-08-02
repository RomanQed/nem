package com.github.romanqed.nem;

import com.github.romanqed.nem.config.NemConfig;
import com.github.romanqed.nem.machine.*;
import com.github.romanqed.nem.util.AdvancedMachineGenerator;
import com.github.romanqed.nem.util.TileClassGenerator;
import ic2.core.block.TileEntityBlock;

public final class MachineUtil {
    // Length
    public static final int FURNACE_LENGTH = 100;
    public static final int MACERATOR_LENGTH = 300;
    public static final int EXTRACTOR_LENGTH = 300;
    public static final int FORMER_LENGTH = 200;
    public static final int COMPRESSOR_LENGTH = 300;
    public static final int RECYCLER_LENGTH = 45;

    // Energy
    public static final int FURNACE_ENERGY = 3;
    public static final int MACERATOR_ENERGY = 2;
    public static final int EXTRACTOR_ENERGY = 2;
    public static final int FORMER_ENERGY = 10;
    public static final int COMPRESSOR_ENERGY = 2;
    public static final int RECYCLER_ENERGY = 1;

    private static final TileClassGenerator GENERATOR = new AdvancedMachineGenerator(MachineUtil.class);

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

    public static Class<TileEntityBlock> createMachine(Class<?> teClass,
                                                       int baseEnergy,
                                                       int length,
                                                       int slots) {
        String name = teClass.getSimpleName() + slots;
        int energy = calculateEnergyPerTick(baseEnergy, slots);
        IC2Tier tier = getTier(slots);
        return GENERATOR.generate(teClass, name, new Object[]{energy, length, tier.getValue(), slots});
    }

    public static Class<TileEntityBlock> createFurnace(int slots) {
        return createMachine(AdvancedFurnace.class, FURNACE_ENERGY, FURNACE_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createMacerator(int slots) {
        return createMachine(AdvancedMacerator.class, MACERATOR_ENERGY, MACERATOR_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createExtractor(int slots) {
        return createMachine(AdvancedExtractor.class, EXTRACTOR_ENERGY, EXTRACTOR_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createCutter(int slots) {
        return createMachine(AdvancedCutter.class, FORMER_ENERGY, FORMER_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createExtruder(int slots) {
        return createMachine(AdvancedExtruder.class, FORMER_ENERGY, FORMER_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createRoller(int slots) {
        return createMachine(AdvancedRoller.class, FORMER_ENERGY, FORMER_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createCompressor(int slots) {
        return createMachine(AdvancedCompressor.class, COMPRESSOR_ENERGY, COMPRESSOR_LENGTH, slots);
    }

    public static Class<TileEntityBlock> createRecycler(int slots) {
        return createMachine(AdvancedRecycler.class, RECYCLER_ENERGY, RECYCLER_LENGTH, slots);
    }
}

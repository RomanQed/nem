package com.github.romanqed.nem;

import com.github.romanqed.nem.generator.AdvancedMachineGenerator;
import com.github.romanqed.nem.generator.MachineGenerator;
import com.github.romanqed.nem.machine.*;
import ic2.core.block.TileEntityBlock;

public final class MachineUtil {
    private static final MachineGenerator GENERATOR = new AdvancedMachineGenerator(MachineUtil.class);
    private static final String ADVANCED_FURNACE = "AdvancedFurnace";
    private static final String ADVANCED_MACERATOR = "AdvancedMacerator";
    private static final String ADVANCED_EXTRACTOR = "AdvancedExtractor";
    private static final String ADVANCED_CUTTER = "AdvancedCutter";
    private static final String ADVANCED_EXTRUDER = "AdvancedExtruder";
    private static final String ADVANCED_ROLLER = "AdvancedRoller";
    private static final String ADVANCED_COMPRESSOR = "AdvancedCompressor";

    private MachineUtil() {
    }

    public static Class<TileEntityBlock> createFurnace(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedFurnace.class,
                ADVANCED_FURNACE + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }

    public static Class<TileEntityBlock> createMacerator(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedMacerator.class,
                ADVANCED_MACERATOR + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }

    public static Class<TileEntityBlock> createExtractor(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedExtractor.class,
                ADVANCED_EXTRACTOR + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }

    public static Class<TileEntityBlock> createCutter(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedCutter.class,
                ADVANCED_CUTTER + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }

    public static Class<TileEntityBlock> createExtruder(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedExtruder.class,
                ADVANCED_EXTRUDER + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }

    public static Class<TileEntityBlock> createRoller(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedRoller.class,
                ADVANCED_ROLLER + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }

    public static Class<TileEntityBlock> createCompressor(int energyPerTick, int length, int tier, int slots) {
        return GENERATOR.generate(
                AdvancedCompressor.class,
                ADVANCED_COMPRESSOR + slots,
                new Object[]{energyPerTick, length, tier, slots}
        );
    }
}

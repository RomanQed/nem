package com.github.romanqed.nem;

import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.IC2Material;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

public enum NemMachine implements ITeBlock {
    // Furnaces
    ADVANCED_FURNACE3(MachineUtil.createFurnace(3), EnumRarity.COMMON),
    ADVANCED_FURNACE6(MachineUtil.createFurnace(6), EnumRarity.UNCOMMON),
    ADVANCED_FURNACE9(MachineUtil.createFurnace(9), EnumRarity.RARE),

    // Macerators
    ADVANCED_MACERATOR3(MachineUtil.createMacerator(3), EnumRarity.COMMON),
    ADVANCED_MACERATOR6(MachineUtil.createMacerator(6), EnumRarity.UNCOMMON),
    ADVANCED_MACERATOR9(MachineUtil.createMacerator(9), EnumRarity.RARE),

    // Extractors
    ADVANCED_EXTRACTOR3(MachineUtil.createExtractor(3), EnumRarity.COMMON),
    ADVANCED_EXTRACTOR6(MachineUtil.createExtractor(6), EnumRarity.UNCOMMON),
    ADVANCED_EXTRACTOR9(MachineUtil.createExtractor(9), EnumRarity.RARE),

    // Cutters
    ADVANCED_CUTTER3(MachineUtil.createCutter(3), EnumRarity.COMMON),
    ADVANCED_CUTTER6(MachineUtil.createCutter(6), EnumRarity.UNCOMMON),
    ADVANCED_CUTTER9(MachineUtil.createCutter(9), EnumRarity.RARE),

    // Extruders
    ADVANCED_EXTRUDER3(MachineUtil.createExtruder(3), EnumRarity.COMMON),
    ADVANCED_EXTRUDER6(MachineUtil.createExtruder(6), EnumRarity.UNCOMMON),
    ADVANCED_EXTRUDER9(MachineUtil.createExtruder(9), EnumRarity.RARE),

    // Rollers
    ADVANCED_ROLLER3(MachineUtil.createRoller(3), EnumRarity.COMMON),
    ADVANCED_ROLLER6(MachineUtil.createRoller(6), EnumRarity.UNCOMMON),
    ADVANCED_ROLLER9(MachineUtil.createRoller(9), EnumRarity.RARE),

    // Compressors
    ADVANCED_COMPRESSOR3(MachineUtil.createCompressor(3), EnumRarity.COMMON),
    ADVANCED_COMPRESSOR6(MachineUtil.createCompressor(6), EnumRarity.UNCOMMON),
    ADVANCED_COMPRESSOR9(MachineUtil.createCompressor(9), EnumRarity.RARE);


    public static final ResourceLocation IDENTIFIER = new ResourceLocation(NemMod.MODID, "machine");

    private final String name;
    private final Class<? extends TileEntityBlock> teClass;
    private final EnumRarity rarity;
    private final float hardness;
    private final float resistance;
    private final TeBlock.DefaultDrop defaultDrop;
    private TileEntityBlock dummyTe;

    NemMachine(Class<? extends TileEntityBlock> teClass, EnumRarity rarity, TeBlock.DefaultDrop drop) {
        this.name = name().toLowerCase();
        this.teClass = teClass;
        this.rarity = rarity;
        this.hardness = 2;
        this.resistance = 10;
        this.defaultDrop = drop;
    }

    NemMachine(Class<? extends TileEntityBlock> teClass, EnumRarity rarity) {
        this(teClass, rarity, TeBlock.DefaultDrop.Machine);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    public ResourceLocation getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public boolean hasItem() {
        return true;
    }

    @Nullable
    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return teClass;
    }

    @Override
    public boolean hasActive() {
        return true;
    }

    @Override
    public Set<EnumFacing> getSupportedFacings() {
        return Util.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return hardness;
    }

    @Override
    public float getExplosionResistance() {
        return resistance;
    }

    @Override
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Wrench;
    }

    @Override
    public TeBlock.DefaultDrop getDefaultDrop() {
        return defaultDrop;
    }

    @Override
    public EnumRarity getRarity() {
        return rarity;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return IC2Material.MACHINE;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    public void buildDummyTe() {
        this.dummyTe = TileEntityBlock.instantiate(teClass);
    }

    public void registerTileEntity() {
        TileEntity.register(NemMod.MODID + ':' + name, teClass);
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public TileEntityBlock getDummyTe() {
        return dummyTe;
    }
}

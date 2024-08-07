package com.github.romanqed.nem;

import com.github.romanqed.nem.generator.ReactorKineticGenerator;
import com.github.romanqed.nem.generator.ReactorStirlingGenerator;
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

public enum NemGenerator implements ITeBlock {
    REACTOR_STIRLING_GENERATOR(ReactorStirlingGenerator.class, EnumRarity.UNCOMMON),
    REACTOR_KINETIC_GENERATOR(ReactorKineticGenerator.class, EnumRarity.UNCOMMON);

    public static final ResourceLocation IDENTIFIER = new ResourceLocation(NemMod.MODID, "generator");

    private final String name;
    private final Class<? extends TileEntityBlock> teClass;
    private final EnumRarity rarity;
    private final float hardness;
    private final float resistance;
    private final TeBlock.DefaultDrop defaultDrop;
    private TileEntityBlock dummyTe;

    NemGenerator(Class<? extends TileEntityBlock> teClass, EnumRarity rarity, TeBlock.DefaultDrop drop) {
        this.name = name().toLowerCase();
        this.teClass = teClass;
        this.rarity = rarity;
        this.hardness = 2;
        this.resistance = 10;
        this.defaultDrop = drop;
    }

    NemGenerator(Class<? extends TileEntityBlock> teClass, EnumRarity rarity) {
        this(teClass, rarity, TeBlock.DefaultDrop.Generator);
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name;
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
        return Util.allFacings;
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

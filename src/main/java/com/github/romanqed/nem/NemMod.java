package com.github.romanqed.nem;

import com.github.romanqed.nem.config.NemConfig;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import ic2.core.ref.TeBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NemMod.MODID,
        name = NemMod.NAME,
        version = NemMod.VERSION,
        dependencies = "required-after:ic2;after:*")
public final class NemMod {
    public static final String MODID = "nem";
    public static final String NAME = "Not Enough Machines";
    public static final String VERSION = "1.2";
    public static final CreativeTab CREATIVE_TAB = new CreativeTab(MODID);

    private static Logger logger;

    private static void setCreativeTabIcon(ITeBlock block) {
        BlockTileEntity teBlock = TeBlockRegistry.get(block.getIdentifier());
        CREATIVE_TAB.setTabIconItemStack(teBlock.getItemStack(block));
    }

    private static void registerMachines() {
        if (!NemConfig.enableMachines) {
            return;
        }
        for (NemMachine block : NemMachine.values()) {
            logger.debug("Register tile entity for {}", block);
            block.registerTileEntity();
        }
    }

    private static void registerGenerators() {
        if (!NemConfig.enableGenerators) {
            return;
        }
        for (NemGenerator block : NemGenerator.values()) {
            logger.debug("Register tile entity for {}", block);
            block.registerTileEntity();
        }
    }

    private static void initMachines() {
        if (!NemConfig.enableMachines) {
            return;
        }
        for (NemMachine machine : NemMachine.values()) {
            logger.debug("Build dummy te for {}", machine);
            machine.buildDummyTe();
        }
    }

    private static void initGenerators() {
        if (!NemConfig.enableGenerators) {
            return;
        }
        for (NemGenerator generator : NemGenerator.values()) {
            logger.debug("Build dummy te for {}", generator);
            generator.buildDummyTe();
        }
    }

    private static ITeBlock selectIcon() {
        if (NemConfig.enableMachines) {
            return NemMachine.ADVANCED_FURNACE3;
        }
        if (NemConfig.enableGenerators) {
            return NemGenerator.REACTOR_KINETIC_GENERATOR;
        }
        return TeBlock.iron_furnace;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        registerMachines();
        registerGenerators();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        initGenerators();
        initMachines();
        setCreativeTabIcon(selectIcon());
    }
}

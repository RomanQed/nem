package com.github.romanqed.nem;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
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
    public static final String VERSION = "1.0";
    public static final CreativeTab CREATIVE_TAB = new CreativeTab(MODID);

    private static Logger logger;

    private static void setCreativeTabIcon(NemMachine machine) {
        BlockTileEntity teBlock = TeBlockRegistry.get(NemMachine.IDENTIFIER);
        CREATIVE_TAB.setTabIconItemStack(teBlock.getItemStack(machine));
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        for (NemMachine block : NemMachine.values()) {
            block.registerTileEntity();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        for (NemMachine machine : NemMachine.values()) {
            machine.buildDummyTe();
        }
        setCreativeTabIcon(NemMachine.ADVANCED_FURNACE3);
    }
}

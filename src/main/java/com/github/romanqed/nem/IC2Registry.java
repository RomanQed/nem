package com.github.romanqed.nem;

import com.github.romanqed.nem.config.NemConfig;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.ITeBlock;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = NemMod.MODID)
public final class IC2Registry {

    @SubscribeEvent
    public static void onTeBlockInit(TeBlockFinalCallEvent event) {
        if (NemConfig.enableMachines) {
            register(NemMachine.class, NemMachine.IDENTIFIER);
        }
        if (NemConfig.enableGenerators) {
            register(NemGenerator.class, NemGenerator.IDENTIFIER);
        }
    }

    private static <E extends Enum<E> & ITeBlock> void register(Class<E> type, ResourceLocation location) {
        TeBlockRegistry.addAll(type, location);
        TeBlockRegistry.addCreativeRegisterer(
                (list, block, item, tab) -> registerCreativeTab(list, block, tab),
                location
        );
    }

    private static void registerCreativeTab(NonNullList<ItemStack> list, BlockTileEntity block, CreativeTabs tab) {
        if (tab != CreativeTabs.SEARCH && tab != NemMod.CREATIVE_TAB) {
            return;
        }
        for (ITeBlock type : block.getAllTypes()) {
            if (!type.hasItem()) {
                continue;
            }
            list.add(block.getItemStack(type));
        }
    }
}
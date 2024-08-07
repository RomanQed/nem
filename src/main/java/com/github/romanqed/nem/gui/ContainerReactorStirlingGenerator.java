package com.github.romanqed.nem.gui;

import com.github.romanqed.nem.generator.ReactorStirlingGenerator;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerReactorStirlingGenerator extends ContainerFullInv<ReactorStirlingGenerator> {

    public ContainerReactorStirlingGenerator(EntityPlayer player, ReactorStirlingGenerator te) {
        super(player, te, 204);
        this.addSlotToContainer(new SlotInvSlot(te.coolFluidInputSlot, 0, 8, 103));
        this.addSlotToContainer(new SlotInvSlot(te.coolOutputSlot, 0, 26, 103));
        this.addSlotToContainer(new SlotInvSlot(te.hotFluidInputSlot, 0, 134, 103));
        this.addSlotToContainer(new SlotInvSlot(te.hotOutputSlot, 0, 152, 103));
        // for-each vectorization
        this.addSlotToContainer(new SlotInvSlot(te.upgradeSlot, 0, 62, 103));
        this.addSlotToContainer(new SlotInvSlot(te.upgradeSlot, 1, 62 + 18, 103));
        this.addSlotToContainer(new SlotInvSlot(te.upgradeSlot, 2, 62 + 2 * 18, 103));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("inputTank");
        ret.add("outputTank");
        return ret;
    }
}

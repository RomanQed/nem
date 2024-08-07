package com.github.romanqed.nem.gui;

import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import net.minecraft.util.ResourceLocation;

public class GuiReactorStirlingGenerator extends GuiIC2<ContainerReactorStirlingGenerator> {

    public GuiReactorStirlingGenerator(ContainerReactorStirlingGenerator container) {
        super(container, 204);
        this.addElement(
                TankGauge.createPlain(this, 19, 47, 12, 44, container.base.getInputTank())
        );
        this.addElement(
                TankGauge.createPlain(this, 145, 47, 12, 44, container.base.getOutputTank())
        );
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation("ic2", "textures/gui/GUIStirlingKineticGenerator.png");
    }
}

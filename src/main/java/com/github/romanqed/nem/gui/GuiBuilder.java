package com.github.romanqed.nem.gui;

import ic2.core.gui.dynamic.GuiParser.GuiNode;

public interface GuiBuilder {

    GuiBuilder setBaseClass(Class<?> baseClass);

    GuiBuilder setName(String name);

    GuiBuilder setProgress(String name, String style, String event);

    GuiBuilder addInputSlot(String name);

    GuiBuilder addOutputSlot(String name);

    GuiNode build();
}

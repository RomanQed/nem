package com.github.romanqed.nem.gui;

import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.GuiParser.*;
import org.xml.sax.Attributes;

import java.lang.reflect.Constructor;

public final class Nodes {
    private static final Constructor<?> GUI_NODE_CTOR
            = lookupConstructor(GuiNode.class, Attributes.class, Class.class);
    private static final Constructor<?> BUTTON_NODE_CTOR
            = lookupConstructor(ButtonNode.class, ParentNode.class, Attributes.class);
    private static final Constructor<?> SLOT_NODE_CTOR
            = lookupConstructor(SlotNode.class, ParentNode.class, Attributes.class);
    private static final Constructor<?> SLOT_GRID_NODE_CTOR
            = lookupConstructor(SlotGridNode.class, ParentNode.class, Attributes.class);
    private static final Constructor<?> TEXT_NODE_CTOR
            = lookupConstructor(TextNode.class, ParentNode.class, Attributes.class);
    private static final Constructor<?> ENERGY_GAUGE_NODE_CTOR
            = lookupConstructor(EnergyGaugeNode.class, ParentNode.class, Attributes.class);
    private static final Constructor<?> ENVIRONMENT_NODE_CTOR
            = lookupConstructor(EnvironmentNode.class, ParentNode.class, Attributes.class);
    private static final Constructor<?> TOOLTIP_NODE_CTOR
            = lookupConstructor(TooltipNode.class, ParentNode.class, String.class);
    private static final Constructor<?> PLAYER_INVENTORY_NODE_CTOR
            = lookupConstructor(getPlayerInventoryClass(), ParentNode.class, Attributes.class);
    private static final Constructor<?> GAUGE_NODE_CTOR
            = lookupConstructor(GaugeNode.class, ParentNode.class, Attributes.class);

    private Nodes() {
    }

    private static Class<?> getPlayerInventoryClass() {
        try {
            ClassLoader loader = GuiParser.class.getClassLoader();
            return Class.forName("ic2.core.gui.dynamic.GuiParser$PlayerInventoryNode", false, loader);
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static Constructor<?> lookupConstructor(Class<?> clazz, Class<?>... params) {
        try {
            Constructor<?> ret = clazz.getDeclaredConstructor(params);
            ret.setAccessible(true);
            return ret;
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiate(Constructor<?> constructor, Object... args) {
        try {
            return (T) constructor.newInstance(args);
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static GuiNode createGuiNode(Class<?> baseClass, Attributes attributes) {
        return instantiate(GUI_NODE_CTOR, attributes, baseClass);
    }

    public static ButtonNode createButtonNode(ParentNode node, Attributes attributes) {
        return instantiate(BUTTON_NODE_CTOR, node, attributes);
    }

    public static SlotNode createSlotNode(ParentNode node, Attributes attributes) {
        return instantiate(SLOT_NODE_CTOR, node, attributes);
    }

    public static SlotGridNode createSlotGridNode(ParentNode node, Attributes attributes) {
        return instantiate(SLOT_GRID_NODE_CTOR, node, attributes);
    }

    public static TextNode createTextNode(ParentNode node, Attributes attributes) {
        return instantiate(TEXT_NODE_CTOR, node, attributes);
    }

    public static EnergyGaugeNode createEnergyGaugeNode(ParentNode node, Attributes attributes) {
        return instantiate(ENERGY_GAUGE_NODE_CTOR, node, attributes);
    }

    public static EnvironmentNode createEnvironmentNode(ParentNode node, Attributes attributes) {
        return instantiate(ENVIRONMENT_NODE_CTOR, node, attributes);
    }

    public static TooltipNode createTooltipNode(ParentNode node, Attributes attributes) {
        return instantiate(TOOLTIP_NODE_CTOR, node, attributes);
    }

    public static Node createPlayerInventoryNode(ParentNode node, Attributes attributes) {
        return instantiate(PLAYER_INVENTORY_NODE_CTOR, node, attributes);
    }

    public static Node createGaugeNode(ParentNode node, Attributes attributes) {
        return instantiate(GAUGE_NODE_CTOR, node, attributes);
    }
}

package com.github.romanqed.nem.gui;

import ic2.core.gui.EnergyGauge.EnergyGaugeStyle;
import ic2.core.gui.Gauge.GaugeProperties;
import ic2.core.gui.Gauge.GaugeStyle;
import ic2.core.gui.Gauge.IGaugeStyle;
import ic2.core.gui.SlotGrid.SlotStyle;
import ic2.core.gui.dynamic.GuiParser.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;

public class AdvancedMachineGuiBuilder implements GuiBuilder {
    private static final String DEFAULT_NAME = "%name%";
    private static final String SLOT_STYLE_NAME = "normal";
    private static final SlotStyle SLOT_STYLE = SlotStyle.Normal;
    private static final EnergyGaugeStyle ENERGY_STYLE = EnergyGaugeStyle.Bolt;
    private static final Attributes GAME_ENVIRONMENT_ATTRIBUTES = new MapAttributes(
            Collections.singletonMap("name", "game")
    );
    private static final Attributes UPGRADES_ATTRIBUTES = getUpgradesAttributes();
    private static final String PLAYER_INVENTORY_X = "7";
    private static final String PLAYER_INVENTORY_Y = "83";
    private static final int WIDTH = 151;
    private static final int HEIGHT = 83;
    private static final int CENTER_X = 75;
    private static final int SLOT_GROUP_WIDTH = SLOT_STYLE.width * 3 - 2;
    private static final int SLOT_GROUP_HEIGHT = SLOT_STYLE.height * 3 - 2;
    private static final int OFFSET = 3;
    private static final int START_OFFSET = 5;

    private final List<String> inputs;
    private final List<String> outputs;
    private Class<?> baseClass;
    private String name;
    private String progressName;
    private String progressStyle;
    private String progressEvent;

    public AdvancedMachineGuiBuilder() {
        this.name = DEFAULT_NAME;
        this.inputs = new LinkedList<>();
        this.outputs = new LinkedList<>();
    }

    private static Attributes getUpgradesAttributes() {
        Map<String, String> ret = new HashMap<>();
        ret.put("name", "upgrade");
        ret.put("x", "151");
        ret.put("y", "7");
        ret.put("cols", "1");
        return new MapAttributes(ret);
    }

    private static List<Attributes> generateSlotGroup(int x, int y, List<String> slots) {
        int size = slots.size();
        if (size < 1 || size > 9) {
            throw new IllegalArgumentException("Unsupported slot amount: " + size);
        }
        List<Attributes> ret = new LinkedList<>();
        int width = SLOT_STYLE.width;
        int height = SLOT_STYLE.height;
        if (size <= 3) {
            fillLine(ret, slots, x, y + height, width);
            return ret;
        }
        if (size <= 6) {
            int offset = SLOT_STYLE.height / 2;
            fillLine(ret, slots.subList(0, 3), x, y + offset, width);
            fillLine(ret, slots.subList(3, size), x, y + offset + height - 1, width);
            return ret;
        }
        fillLine(ret, slots.subList(0, 3), x, y, width);
        fillLine(ret, slots.subList(3, 6), x, y + height - 1, width);
        fillLine(ret, slots.subList(6, size), x, y + height + height - 2, width);
        return ret;
    }

    private static void fillLine(List<Attributes> nodes, List<String> slots, int x, int y, int width) {
        if (slots.isEmpty()) {
            return;
        }
        int size = slots.size();
        if (size == 1) {
            nodes.add(getSlotAttrs(x + width, y, slots.get(0)));
            return;
        }
        if (size == 2) {
            int offset = width / 2;
            nodes.add(getSlotAttrs(x + offset, y, slots.get(0)));
            nodes.add(getSlotAttrs(x + offset + width - 1, y, slots.get(1)));
            return;
        }
        nodes.add(getSlotAttrs(x, y, slots.get(0)));
        nodes.add(getSlotAttrs(x + width - 1, y, slots.get(1)));
        nodes.add(getSlotAttrs(x + width + width - 2, y, slots.get(2)));
    }

    private static Attributes getSlotAttrs(int x, int y, String name) {
        Map<String, String> ret = new HashMap<>();
        ret.put("x", Integer.toString(x));
        ret.put("y", Integer.toString(y));
        ret.put("name", name);
        ret.put("style", SLOT_STYLE_NAME);
        return new MapAttributes(ret);
    }

    private static Attributes getRecipeButtonAttributes(int x, int y, IGaugeStyle style, String event) {
        Map<String, String> ret = new HashMap<>();
        ret.put("type", "recipe");
        ret.put("x", Integer.toString(x));
        ret.put("y", Integer.toString(y));
        GaugeProperties properties = style.getProperties();
        ret.put("width", Integer.toString(properties.innerWidth));
        ret.put("height", Integer.toString(properties.innerHeight));
        ret.put("event", event);
        return new MapAttributes(ret);
    }

    private static Attributes getEnergyGaugeAttributes(int x, int y) {
        Map<String, String> ret = new HashMap<>();
        ret.put("x", Integer.toString(x));
        ret.put("y", Integer.toString(y));
        return new MapAttributes(ret);
    }

    private static Attributes getProgressAttributes(String name, int x, int y, String style) {
        Map<String, String> ret = new HashMap<>();
        ret.put("name", name);
        ret.put("x", Integer.toString(x));
        ret.put("y", Integer.toString(y));
        ret.put("style", style);
        return new MapAttributes(ret);
    }

    private static EnvironmentNode addGameEnvironment(ParentNode node) {
        EnvironmentNode ret = Nodes.createEnvironmentNode(node, GAME_ENVIRONMENT_ATTRIBUTES);
        node.addNode(ret);
        return ret;
    }

    private void reset() {
        this.inputs.clear();
        this.outputs.clear();
        this.name = DEFAULT_NAME;
        this.baseClass = null;
        this.progressName = null;
        this.progressStyle = null;
        this.progressEvent = null;
    }

    @Override
    public GuiBuilder setBaseClass(Class<?> baseClass) {
        this.baseClass = baseClass;
        return this;
    }

    @Override
    public GuiBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public GuiBuilder setProgress(String name, String style, String event) {
        this.progressName = name;
        this.progressStyle = style;
        this.progressEvent = event;
        return this;
    }

    @Override
    public GuiBuilder addInputSlot(String name) {
        this.inputs.add(name);
        return this;
    }

    @Override
    public GuiBuilder addOutputSlot(String name) {
        this.outputs.add(name);
        return this;
    }

    private GuiNode createGuiNode() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("width", "176");
        attributes.put("height", "166");
        return Nodes.createGuiNode(baseClass, new MapAttributes(attributes));
    }

    private void addPlayerInventoryNode(ParentNode parent) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("x", PLAYER_INVENTORY_X);
        attributes.put("y", PLAYER_INVENTORY_Y);
        Node node = Nodes.createPlayerInventoryNode(parent, new MapAttributes(attributes));
        parent.addNode(node);
    }

    private void addUpgradeSlots(ParentNode parent) {
        Node node = Nodes.createSlotGridNode(parent, UPGRADES_ATTRIBUTES);
        parent.addNode(node);
    }

    private void addRecipeButton(ParentNode parent, int x, int y, IGaugeStyle style) {
        if (progressEvent == null) {
            return;
        }
        Attributes attrs = getRecipeButtonAttributes(x, y, style, progressEvent);
        Node node = Nodes.createButtonNode(parent, attrs);
        parent.addNode(node);
    }

    private void addEnergyGauge(ParentNode parent, int x, int y) {
        Node node = Nodes.createEnergyGaugeNode(parent, getEnergyGaugeAttributes(x, y));
        parent.addNode(node);
    }

    private void addProgressBar(ParentNode parent, int x, int y) {
        Node node = Nodes.createGaugeNode(parent, getProgressAttributes(progressName, x, y, progressStyle));
        parent.addNode(node);
    }

    private void addInputSlots(ParentNode node, int x, int y) {
        List<Attributes> slots = generateSlotGroup(x, y, this.inputs);
        for (Attributes attr : slots) {
            Node slot = Nodes.createSlotNode(node, attr);
            node.addNode(slot);
        }
    }

    private void addOutputSlots(ParentNode node, int x, int y) {
        List<Attributes> slots = generateSlotGroup(x, y, this.outputs);
        for (Attributes attr : slots) {
            Node slot = Nodes.createSlotNode(node, attr);
            node.addNode(slot);
        }
    }

    private void addDischargeSlot(ParentNode parent, int x, int y) {
        Attributes attrs = getSlotAttrs(x, y, "discharge");
        Node node = Nodes.createSlotNode(parent, attrs);
        parent.addNode(node);
    }

    private int addCenterGroup(ParentNode gui, ParentNode env, IGaugeStyle style) {
        GaugeProperties progressProps = style.getProperties();
        GaugeProperties energyProps = ENERGY_STYLE.properties;
        int energyHeight = energyProps.innerHeight;
        int progressHeight = progressProps.innerHeight;
        int common = energyHeight + progressHeight + SLOT_STYLE.height + 10;
        int yOffset = START_OFFSET + (HEIGHT - common) / 2;
        int progressX = CENTER_X - progressProps.innerWidth / 2;
        addProgressBar(gui, progressX, yOffset);
        addRecipeButton(env, progressX, yOffset, style);
        addEnergyGauge(gui,
                CENTER_X - energyProps.innerWidth / 2,
                yOffset + progressHeight + OFFSET
        );
        addDischargeSlot(
                gui,
                CENTER_X - SLOT_STYLE.height / 2,
                yOffset + energyHeight + progressHeight + OFFSET + OFFSET
        );
        return progressX;
    }

    private void addName(ParentNode parent) {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("y", "6");
        attrs.put("align", "center");
        TextNode node = Nodes.createTextNode(parent, new MapAttributes(attrs));
        try {
            node.setContent(name);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        parent.addNode(node);
    }

    @Override
    public GuiNode build() {
        Objects.requireNonNull(baseClass);
        Objects.requireNonNull(name);
        Objects.requireNonNull(progressName);
        Objects.requireNonNull(progressStyle);
        try {
            GuiNode ret = createGuiNode();
            // Add name
            addName(ret);
            // Add player inventory
            addPlayerInventoryNode(ret);
            // Add envied
            ParentNode env = addGameEnvironment(ret);
            addUpgradeSlots(env);
            // Add center things
            int progressX = addCenterGroup(ret, env, GaugeStyle.get(progressStyle));
            // Add slot groups
            int heightOffset = (HEIGHT - SLOT_GROUP_HEIGHT) / 2;
            int widthOffset = (progressX - SLOT_GROUP_WIDTH) / 2;
            addInputSlots(ret, widthOffset, heightOffset);
            addOutputSlots(ret, WIDTH - widthOffset - SLOT_GROUP_WIDTH, heightOffset);
            return ret;
        } finally {
            reset();
        }
    }
}

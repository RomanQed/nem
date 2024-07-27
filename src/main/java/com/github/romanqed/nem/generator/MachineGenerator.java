package com.github.romanqed.nem.generator;

import ic2.core.block.TileEntityBlock;

public interface MachineGenerator {

    Class<TileEntityBlock> generate(Class<?> base, String name, Object[] data);
}

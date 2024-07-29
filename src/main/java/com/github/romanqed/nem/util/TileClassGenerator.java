package com.github.romanqed.nem.util;

import ic2.core.block.TileEntityBlock;

public interface TileClassGenerator {

    Class<TileEntityBlock> generate(Class<?> base, String name, Object[] data);
}

package com.github.romanqed.nem.util;

import ic2.core.block.TileEntityBlock;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class AbstractTileGenerator implements TileClassGenerator {
    protected final DefineLoader loader;

    public AbstractTileGenerator(DefineLoader loader) {
        this.loader = loader;
    }

    public AbstractTileGenerator(Class<?> context) {
        this(WrapClassLoader.of(context.getClassLoader()));
    }

    protected abstract void generateCtor(MethodVisitor visitor, String parent, String name, Object[] data);

    @Override
    @SuppressWarnings("unchecked")
    public Class<TileEntityBlock> generate(Class<?> base, String name, Object[] data) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        String parent = Type.getInternalName(base);
        // Visit class header
        writer.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                name,
                null,
                parent,
                null
        );
        // Visit ctor
        MethodVisitor cv = writer.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        cv.visitCode();
        generateCtor(cv, parent, name, data);
        cv.visitMaxs(0, 0);
        cv.visitEnd();
        // Load & return class
        return (Class<TileEntityBlock>) loader.define(name, writer.toByteArray());
    }
}

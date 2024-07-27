package com.github.romanqed.nem.generator;

import com.github.romanqed.nem.util.DefineLoader;
import com.github.romanqed.nem.util.WrapClassLoader;
import ic2.core.block.TileEntityBlock;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class AdvancedMachineGenerator implements MachineGenerator {
    private static final String DESCRIPTOR = "(IIII)V";
    private final DefineLoader loader;

    public AdvancedMachineGenerator(DefineLoader loader) {
        this.loader = loader;
    }

    public AdvancedMachineGenerator(Class<?> context) {
        this(WrapClassLoader.of(context.getClassLoader()));
    }

    private static void pushInt(MethodVisitor visitor, int value) {
        if (value >= -1 && value <= 5) {
            visitor.visitInsn(Opcodes.ICONST_M1 + value + 1);
            return;
        }
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            visitor.visitIntInsn(Opcodes.BIPUSH, value);
            return;
        }
        if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            visitor.visitIntInsn(Opcodes.SIPUSH, value);
            return;
        }
        visitor.visitLdcInsn(value);
    }

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
        cv.visitVarInsn(Opcodes.ALOAD, 0);
        if (data.length != 4) {
            throw new IllegalArgumentException("Illegal common advanced machine parameters");
        }
        for (Object argument : data) {
            pushInt(cv, (Integer) argument);
        }
        cv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                parent,
                "<init>",
                DESCRIPTOR,
                false
        );
        cv.visitInsn(Opcodes.RETURN);
        cv.visitMaxs(0, 0);
        cv.visitEnd();
        // Load & return class
        return (Class<TileEntityBlock>) loader.define(name, writer.toByteArray());
    }
}

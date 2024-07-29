package com.github.romanqed.nem.util;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class AdvancedMachineGenerator extends AbstractTileGenerator {
    private static final String DESCRIPTOR = "(IIII)V";

    public AdvancedMachineGenerator(DefineLoader loader) {
        super(loader);
    }

    public AdvancedMachineGenerator(Class<?> context) {
        super(context);
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
    protected void generateCtor(MethodVisitor cv, String parent, String name, Object[] data) {
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
    }
}

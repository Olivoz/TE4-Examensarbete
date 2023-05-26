package io.github.olivoz;

import org.objectweb.asm.*;

public class ASMVisitor extends ClassVisitor {
    public ASMVisitor(ClassVisitor parent) {
        super(Opcodes.ASM9, parent);
    }

    public static byte[] transform(byte[] bytes) {
        final ClassReader classReader = new ClassReader(bytes);
        final ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classReader.accept(new ASMVisitor(cw), 0);
        return cw.toByteArray();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (!name.equals("sayHello") || !descriptor.equals("()V")) return methodVisitor;

        return new SayHelloMethodReplacer(methodVisitor);
    }
}

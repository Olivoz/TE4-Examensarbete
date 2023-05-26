package io.github.olivoz;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class SayHelloMethodReplacer extends MethodVisitor {

    private final MethodVisitor target;

    public SayHelloMethodReplacer(MethodVisitor target) {
        super(Opcodes.ASM9);
        this.target = target;
    }

    @Override
    public void visitCode() {
        target.visitCode();

        // GETSTATIC java/lang/System.out Ljava/io/PrintStream;
        target.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // LDC “Good Bye :c"
        target.visitLdcInsn("Good Bye :c");

        // INVOKEVIRTUAL java/io/PrintStream.println(Ljava/lang/String;)V
        target.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        // RETURN
        target.visitInsn(Opcodes.RETURN);

        // Ange antalet variabler. I det här fallet finns det
        // Inga lokala eller globala variabler.
        target.visitMaxs(0, 0);
        target.visitEnd();
    }
}

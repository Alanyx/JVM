//package com.alany.analysejvm.lecture9.demo2;
//
//import jdk.internal.org.objectweb.asm.*;
//
//import java.io.IOException;
//import java.lang.invoke.CallSite;
//import java.lang.invoke.MethodHandles;
//import java.lang.invoke.MethodType;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
///**
// 你无需理解上面这段代码的具体含义，只须了解它会更改同一目录下 Circuit 类的 startRace(Object) 方法，
// 使之包含 invokedynamic 指令，执行所谓的赛跑方法。
// * @author yinxing
// * @date 2019/12/9
// */
//
//public class ASMHelper implements Opcodes {
//
//    private static class MyMethodVisitor extends MethodVisitor {
//
//        private static final String BOOTSTRAP_CLASS_NAME = Circuit.class.getName().replace('.', '/');
//        private static final String BOOTSTRAP_METHOD_NAME = "bootstrap";
//        private static final String BOOTSTRAP_METHOD_DESC = MethodType
//                .methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class)
//                .toMethodDescriptorString();
//
//        private static final String TARGET_METHOD_NAME = "race";
//        private static final String TARGET_METHOD_DESC = "(Ljava/lang/Object;)V";
//
//        public final MethodVisitor mv;
//
//        public MyMethodVisitor(int api, MethodVisitor mv) {
//            super(api);
//            this.mv = mv;
//        }
//
//        @Override
//        public void visitCode() {
//            mv.visitCode();
//            mv.visitVarInsn(ALOAD, 0);
//            Handle h = new Handle(H_INVOKESTATIC, BOOTSTRAP_CLASS_NAME, BOOTSTRAP_METHOD_NAME, BOOTSTRAP_METHOD_DESC, false);
//            mv.visitInvokeDynamicInsn(TARGET_METHOD_NAME, TARGET_METHOD_DESC, h);
//            mv.visitInsn(RETURN);
//            mv.visitMaxs(1, 1);
//            mv.visitEnd();
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        ClassReader cr = new ClassReader("Circuit");
//        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
//        ClassVisitor cv = new ClassVisitor(ASM6, cw) {
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
//                                             String[] exceptions) {
//                MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
//                if ("startRace".equals(name)) {
//                    return new MyMethodVisitor(ASM6, visitor);
//                }
//                return visitor;
//            }
//        };
//        cr.accept(cv, ClassReader.SKIP_FRAMES);
//
//        Files.write(Paths.get("Circuit.class"), cw.toByteArray());
//    }
//}

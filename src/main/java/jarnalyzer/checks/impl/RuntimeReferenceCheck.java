package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.security.spec.ECField;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RuntimeReferenceCheck implements Check {

    @Override public void checkJarEntry(JarFile src, JarEntry je) {

    }
    void a() throws Exception {
        new ProcessBuilder("ls").inheritIO().start().waitFor();
    }
    @Override public void checkClassFile(ClassNode classFile) throws Exception {
        for (MethodNode method : classFile.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof MethodInsnNode insn) {
                    if (insn.owner.equals("java/lang/Runtime")) {
                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.SUSPICIOUS,Check.stripControl(classFile.name)+"."+Check.stripControl(method.name+method.desc)+" references runtime: "+insn.name+insn.desc);
                        if (insn.name.equals("exec")) {
                            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.DANGER, Check.stripControl(classFile.name)+"."+Check.stripControl(method.name+method.desc)+" runs shell command");
                        }
                    }
                    if (insn.owner.equals("java/lang/ProcessBuilder")) {
                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.DANGER, Check.stripControl(classFile.name)+"."+Check.stripControl(method.name+method.desc)+" builds another process: "+insn.name+insn.desc);
                    }
                }
            }
        }
    }
}

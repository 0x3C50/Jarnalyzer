package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CustomClassLoaderCheck implements Check {

    @Override public void checkJarEntry(JarFile src, JarEntry je) {

    }

    @Override public void checkClassFile(ClassNode classFile) throws Exception {
        if (classFile.superName != null && (classFile.superName.equals("java/lang/ClassLoader") || classFile.superName.equals("java/net/URLClassLoader"))) {
            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.SUSPICIOUS, Check.stripControl(classFile.name)+" extends "+classFile.superName+" with class loading capabilities");
            for (MethodNode method : classFile.methods) {
                for (AbstractInsnNode instruction : method.instructions) {
                    if (instruction instanceof MethodInsnNode min) {
                        if (min.name.equals("defineClass")) {
                            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.DANGER, Check.stripControl(classFile.name)+"."+Check.stripControl(method.name+ method.desc)+" calls "+min.name+min.desc+" to define class at runtime");
                        }
                    }
                }
            }
        }

    }
}

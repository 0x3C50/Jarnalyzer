package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import org.fusesource.jansi.Ansi;
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
            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.DANGER, Ansi.ansi()
                    .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name))
                    .fg(Ansi.Color.WHITE).a(" extends ")
                    .fg(Ansi.Color.BLUE).a(classFile.superName)
                    .fg(Ansi.Color.WHITE).a(" with class loading capabilities").toString());
            for (MethodNode method : classFile.methods) {
                for (AbstractInsnNode instruction : method.instructions) {
                    if (instruction instanceof MethodInsnNode min) {
                        if (min.name.equals("defineClass")) {
                            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.DANGER, Ansi.ansi().fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name) + "." + Check.stripControl(method.name + method.desc)).fg(Ansi.Color.WHITE).a(" calls ").fg(Ansi.Color.BLUE).a(min.name + min.desc).fg(Ansi.Color.WHITE).a(" to define class at runtime").toString());
                        }
                    }
                }
            }
        }

    }
}

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

public class ReflectionCheck implements Check {
    @Override public void checkJarEntry(JarFile src, JarEntry je) {

    }

    @Override public void checkClassFile(ClassNode classFile) throws Exception {
        for (MethodNode method : classFile.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof MethodInsnNode m) {
                    if (m.name.equals("getClass")) {
                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                                .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                .fg(Ansi.Color.WHITE).a(" gets own class object").toString());
//                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Check.stripControl(classFile.name)+"."+Check.stripControl(method.name)+" gets own class object");
                    }
                    if (m.owner.equals("java/lang/Class")) {
                        switch(m.name) {
                            case "getDeclaredMethods" -> Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                                    .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                    .fg(Ansi.Color.WHITE).a(" gets methods from other class").toString());
                            case "getDeclaredFields" -> Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                                    .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                    .fg(Ansi.Color.WHITE).a(" gets fields from other class").toString());
                            case "getDeclaredClasses" -> Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                                    .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                    .fg(Ansi.Color.WHITE).a(" gets inner classes from other class").toString());
                            case "getDeclaredConstructors" -> Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                                    .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                    .fg(Ansi.Color.WHITE).a(" gets class constructors from other class").toString());
                        }
                    } else if (m.owner.equals("java/lang/reflect/Method") && m.name.equals("invoke")) {
                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.SUSPICIOUS, Ansi.ansi()
                                .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                .fg(Ansi.Color.WHITE).a(" invokes other method (invoke())").toString());
//                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.SUSPICIOUS, Check.makeSureNameIsReadable(classFile.name)+"."+Check.stripControl(method.name)+" invokes other method using "+m.name+m.desc);
                    } else if (m.owner.equals("java/lang/reflect/Constructor") && m.name.equals("newInstance")) {
                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.SUSPICIOUS, Ansi.ansi()
                                .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                                .fg(Ansi.Color.WHITE).a(" creates another instance of other class").toString());
                    }
                }
            }
        }
    }
}

package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import jarnalyzer.checks.Checks;
import org.fusesource.jansi.Ansi;
import org.objectweb.asm.tree.*;

import java.awt.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileReferenceCheck implements Check {
    @Override
    public void checkClassFile(ClassNode classFile) throws Exception {
        for (MethodNode method : classFile.methods) {
            for (int index = 0; index < method.instructions.size(); index++) {
                AbstractInsnNode instruction = method.instructions.get(index);
                if (instruction instanceof MethodInsnNode i) {
                    if (i.owner.equals("java/io/File")) {
                        if (i.name.equals("<init>")) {
                            if (index > 1) {
                                AbstractInsnNode hopefullyALdc = method.instructions.get(index-1);
                                if (hopefullyALdc instanceof LdcInsnNode letsGo) {
                                    Logger.logWithSuspicionLevel(Logger.SuspicionLevel.WARNING, Ansi.ansi().a(Check.stripControl(classFile.name+"."+method.name+method.desc)+" creates file reference with "+i.name+i.desc+", references file ").fg(Ansi.Color.BLUE).a(Check.stripControl(letsGo.cst.toString())).toString());
                                } else {
                                    Logger.logWithSuspicionLevel(Logger.SuspicionLevel.WARNING, Check.stripControl(classFile.name+"."+method.name+method.desc)+" creates file reference with "+i.name+i.desc+", references unknown or variable file");
                                }
                            }
                        } else {
                            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.WARNING, Check.stripControl(classFile.name+"."+method.name+method.desc)+" operates on file: "+i.name+i.desc);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void checkJarEntry(JarFile src, JarEntry je) {

    }
}

package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LinkReferenceCheck implements Check {
    @Override public void checkJarEntry(JarFile src, JarEntry je) {

    }

    @Override public void checkClassFile(ClassNode classFile) throws Exception {
        for (MethodNode method : classFile.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof LdcInsnNode ldc) {
                    String val = ldc.cst.toString();
                    if (val.contains(":/") && val.indexOf(":/") != 0) {
                        Logger.logWithSuspicionLevel(Logger.SuspicionLevel.WARNING,"Found link \""+Check.stripControl(val)+"\" in "+Check.stripControl(classFile.name)+"."+Check.stripControl(method.name+method.desc));
                    }
                }
            }
        }
    }
}

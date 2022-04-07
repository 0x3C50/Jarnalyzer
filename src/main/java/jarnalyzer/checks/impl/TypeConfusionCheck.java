package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import javassist.CtClass;
import javassist.bytecode.ClassFile;

import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TypeConfusionCheck implements Check {

    @Override
    public void checkJarEntry(JarFile s, JarEntry je) {
        try {
            InputStream i = s.getInputStream(je);
            boolean isFolder = i.available() == 0;
            if (!isFolder && je.isDirectory()) {
                Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Check.makeSureNameIsReadable(je.getName())+" has type confusion: pretend folder is not actually folder (obfuscation?)");
            }
        } catch (Exception e) {
            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, "Failed to get byte count for "+Check.makeSureNameIsReadable(je.getName()));
        }
    }

    @Override
    public void checkClassFile(CtClass classFile) {

    }
}

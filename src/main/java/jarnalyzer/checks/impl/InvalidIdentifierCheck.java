package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.ClassFile;

import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InvalidIdentifierCheck implements Check {
    static final String[] KEYWORDS = new String[] {
            "abstract","continue","for","new","switch","assert","default","goto","package","synchronized","boolean","do","if","private","this","break","double","implements","protected","throw","byte","else","import","public","throws","case","enum","instanceof","return","transient","catch","extends","int","short","try","char","final","interface","static","void","class","finally","long","strictfp","volatile","const","float","native","super","while"
    };
    @Override
    public void checkJarEntry(JarFile src, JarEntry je) {

    }

    @Override
    public void checkClassFile(CtClass classFile) {
        String[] spl = classFile.getName().split("\\.");
        if (Arrays.stream(spl).anyMatch(s -> Arrays.asList(KEYWORDS).contains(s)) || Arrays.stream(spl).anyMatch(s -> s.isEmpty() || s.isBlank())) {
            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, "Class "+Check.makeSureNameIsReadable(classFile.getName())+" has illegal name (obfuscation?)");
        }
        for (CtMethod declaredMethod : classFile.getDeclaredMethods()) {
            String name = declaredMethod.getName();
            if (Arrays.asList(KEYWORDS).contains(name) || name.isEmpty() || name.isBlank()) {
                Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, "Method "+Check.stripControl(classFile.getName()+"."+name+declaredMethod.getSignature())+" has illegal name (obfuscation?)");
            }
        }
    }
}

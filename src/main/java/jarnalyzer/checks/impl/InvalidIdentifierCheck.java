package jarnalyzer.checks.impl;

import jarnalyzer.Logger;
import jarnalyzer.checks.Check;
import org.fusesource.jansi.Ansi;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InvalidIdentifierCheck implements Check {
    static final String[] KEYWORDS = new String[]{"abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this",
            "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int",
            "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"};

    @Override public void checkJarEntry(JarFile src, JarEntry je) {

    }

    @Override public void checkClassFile(ClassNode classFile) {
        String[] spl = classFile.name.split("/");
        if (Arrays.stream(spl).anyMatch(s -> Arrays.asList(KEYWORDS).contains(s)) || Arrays.stream(spl).anyMatch(s -> s.isEmpty() || s.isBlank())) {
//            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, "Class " + Check.makeSureNameIsReadable(classFile.name) + " has illegal name (obfuscation?)");
            Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                    .fg(Ansi.Color.WHITE).a("Class ")
                    .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name))
                    .fg(Ansi.Color.WHITE).a(" has illegal name (obfuscation?)").toString());
        }
        for (MethodNode method : classFile.methods) {
            String name = method.name;
            if (Arrays.asList(KEYWORDS).contains(name) || name.isEmpty() || name.isBlank()) {
                Logger.logWithSuspicionLevel(Logger.SuspicionLevel.INFO, Ansi.ansi()
                        .fg(Ansi.Color.WHITE).a("Method ")
                        .fg(Ansi.Color.BLUE).a(Check.stripControl(classFile.name + "." + method.name + method.desc))
                        .fg(Ansi.Color.WHITE).a(" has illegal name (obfuscation?)").toString());
            }
        }
    }
}

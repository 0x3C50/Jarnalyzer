package jarnalyzer.checks;

import javassist.CtClass;
import javassist.bytecode.ClassFile;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public interface Check {
    void checkJarEntry(JarFile src, JarEntry je);
    void checkClassFile(CtClass classFile) throws Exception;
    static String stripControl(String s) {
        return s.replaceAll("\\P{Print}", "?");
    }
    static String makeSureNameIsReadable(String s) {
        if (s.length() > 30) {
            int showAtEnd = 10;
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            char[] v = s.toCharArray();
            for (int i = 0; i < v.length; i++) {
                if (i <= showAtEnd) sb1.append(v[i]);
                else if (v.length-i <= showAtEnd) sb2.append(v[i]);
            }
            s = sb1 +" (...) "+ sb2;
        }
        return stripControl(s);
    }
}

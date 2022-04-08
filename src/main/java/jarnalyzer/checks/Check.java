package jarnalyzer.checks;

import org.objectweb.asm.tree.ClassNode;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public interface Check {
    static String stripControl(String s) {
        return s.replaceAll("\\P{Print}", "?");
    }

    static String makeSureNameIsReadable(String s) {
        return stripControl(s);
    }

    void checkJarEntry(JarFile src, JarEntry je);

    void checkClassFile(ClassNode classFile) throws Exception;
}

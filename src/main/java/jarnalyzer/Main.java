package jarnalyzer;

import jarnalyzer.checks.Check;
import jarnalyzer.checks.Checks;
import org.fusesource.jansi.AnsiConsole;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Main {
    static int[] CLASSFILE_SIG = new int[]{0xCA, 0xFE, 0xBA, 0xBE};

    public static void main(String[] args) throws Exception {
        AnsiConsole.systemInstall();
        if (args.length < 1) {
            System.err.println(ansi().fg(RED).a("At least 1 argument required: Input file"));
            return;
        }
        String input = String.join(" ", args);
        File f = new File(input);
        if (!f.exists()) {
            System.err.println(ansi().fg(RED).a("Input file does not exist"));
            return;
        }
        if (!f.isFile()) {
            System.err.println(ansi().fg(RED).a("Input file is not a regular file"));
            return;
        }
        if (!f.canRead()) {
            System.err.println(ansi().fg(RED).a("Can't read input file. Check file permissions"));
            return;
        }
        Logger.printTitle(GREEN, "Scan", 2);
        List<Check> checks = Checks.getChecks();
        JarFile jf = new JarFile(f);
        List<JarEntry> jes = jf.stream().toList();
        for (JarEntry je : jes) {
            for (Check check : checks) {
                check.checkJarEntry(jf, je);
            }
            InputStream is = jf.getInputStream(je);
            if (is.available() > 0) {
                byte[] v = is.readAllBytes();
                if (v.length > 4) {
                    byte[] sigB = Arrays.copyOfRange(v, 0, 4);
                    int[] sig = new int[4];
                    for (int i = 0; i < sigB.length; i++) {
                        sig[i] = Byte.toUnsignedInt(sigB[i]);
                    }
                    if (Arrays.equals(sig, CLASSFILE_SIG)) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(v);
                        ClassReader cr = new ClassReader(v);
                        ClassNode cn = new ClassNode();
                        cr.accept(cn, ClassReader.EXPAND_FRAMES);
                        bais.close();
                        for (Check check : checks) {
                            check.checkClassFile(cn);
                        }
                    }
                }
            }
            is.close();
        }
        Logger.printTitle(GREEN, "Summary", 2);
        Map<Logger.SuspicionLevel, AtomicInteger> v = new ConcurrentHashMap<>();
        int longest = 0;
        for (Logger.SuspicionLevel value : Logger.SuspicionLevel.values()) {
            v.put(value, new AtomicInteger(0));
            longest = Math.max(longest, value.name().length());
        }
        for (Logger.E suspLevel : Logger.suspLevels) {
            v.get(suspLevel.level()).incrementAndGet();
        }
        for (Logger.SuspicionLevel value : Logger.SuspicionLevel.values()) {
            int count = v.get(value).get();
            Logger.log(ansi().fg(value.c).a("[ " + value.name() + " ]").fg(WHITE).a(" ".repeat(longest - value.name().length())).a("  " + count + " event" + (count != 1 ? "s" : "")));
        }
    }
}

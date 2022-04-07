package jarnalyzer;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class Logger {
    record E(SuspicionLevel level, String msg) {}
    public static List<E> suspLevels = new CopyOnWriteArrayList<>();
    public static void log(Object t) {
        System.out.println(t.toString());
    }
    public static void logWithSuspicionLevel(SuspicionLevel level, String t) {
        String t1 = ansi().fg(Ansi.Color.GREEN).a("["+level.name()+"]  ").fg(level.c).a(t).toString();
        suspLevels.add(new E(level, t));
        log(t1);
    }
    public static void printTitle(Ansi.Color color, String t, int padding) {
        String v = "==  "+Arrays.stream(t.toUpperCase().split("")).collect(Collectors.joining(" ".repeat(padding)))+"  ==";
        int w = AnsiConsole.out().getTerminalWidth();
        int widthLeft = w-v.length();
        if (widthLeft > 0) {
            int pv = widthLeft/2;
            v = " ".repeat(pv)+v+" ".repeat(pv);
        }
        System.out.println(ansi().fg(color).a(v));
    }
    public enum SuspicionLevel {
        INFO(Ansi.Color.CYAN), WARNING(Ansi.Color.YELLOW), SUSPICIOUS(Ansi.Color.MAGENTA), DANGER(Ansi.Color.RED);
        final Ansi.Color c;
        SuspicionLevel(Ansi.Color color) {
            this.c = color;
        }

        public Ansi.Color getC() {
            return c;
        }
    }
}

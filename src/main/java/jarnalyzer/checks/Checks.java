package jarnalyzer.checks;

import jarnalyzer.checks.impl.*;

import java.util.ArrayList;
import java.util.List;

public class Checks {
    static List<Check> checks = new ArrayList<>();

    static void init() {
        if (checks.isEmpty()) {
            checks.addAll(List.of(new LinkReferenceCheck(), new FileReferenceCheck(), new ReflectionCheck(), new TypeConfusionCheck(), new InvalidIdentifierCheck(), new RuntimeReferenceCheck(), new CustomClassLoaderCheck()));
        }
    }

    public static List<Check> getChecks() {
        init();
        return checks;
    }
}

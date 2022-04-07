package jarnalyzer.checks;

import jarnalyzer.checks.impl.InvalidIdentifierCheck;
import jarnalyzer.checks.impl.RuntimeReferenceCheck;
import jarnalyzer.checks.impl.TypeConfusionCheck;

import java.util.ArrayList;
import java.util.List;

public class Checks {
    static List<Check> checks = new ArrayList<>();
    static void init() {
        if (checks.isEmpty()) {
            checks.addAll(List.of(
                new TypeConfusionCheck(),
                    new InvalidIdentifierCheck(),
                    new RuntimeReferenceCheck()
            ));
        }
    }
    public static List<Check> getChecks() {
        init();
        return checks;
    }
}

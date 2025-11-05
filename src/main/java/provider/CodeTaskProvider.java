package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.minigame.TextTask;

/**
 * Tiny programs for the Programmer mini-game: pick one from the list and
 * retype it exactly (Factory/Provider pattern).
 *
 * <p>Kept deliberately small (3–6 lines) so it plays in under two minutes.
 * Content is plain pseudo-Python; grading ignores leading/trailing spaces
 * per line but not the code itself.</p>
 */
public final class CodeTaskProvider {

    private static final List<TextTask> TASKS = new ArrayList<>();

    static {
        add(new TextTask("hello", "Hello Program",
                "print(\"Hello, client!\")",
                30, 1));
        add(new TextTask("sum", "Sum Function",
                "def total(a, b):\n    return a + b\n\nprint(total(21, 21))",
                60, 1));
        add(new TextTask("loop", "Counter Loop",
                "for i in range(1, 6):\n    print(f\"Order #{i} ready\")",
                60, 2));
        add(new TextTask("max", "Find Maximum",
                "def biggest(a, b):\n    if a > b:\n        return a\n    return b\n\nprint(biggest(17, 42))",
                90, 2));
        add(new TextTask("discount", "Discount Calculator",
                "def price(amount, vip):\n    off = 0.2 if vip else 0.1\n    return amount * (1 - off)\n\nprint(price(100, True))",
                110, 3));
    }

    private CodeTaskProvider() {
    }

    private static void add(TextTask t) {
        TASKS.add(t);
    }

    public static List<TextTask> getAll() {
        return Collections.unmodifiableList(TASKS);
    }

    public static TextTask getById(String id) {
        return TASKS.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
}

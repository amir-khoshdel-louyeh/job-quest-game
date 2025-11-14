package provider;

import java.util.ArrayList;
import java.util.List;

import model.minigame.TextTask;

// Holds the code tasks for the Programmer mini-game.
public final class CodeTaskProvider {

    private static ArrayList<TextTask> TASKS = new ArrayList<TextTask>();

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
        ArrayList<TextTask> copy = new ArrayList<TextTask>();
        for (int i = 0; i < TASKS.size(); i++) {
            copy.add(TASKS.get(i));
        }
        return copy;
    }

    public static TextTask getById(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < TASKS.size(); i++) {
            TextTask t = TASKS.get(i);
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    // Same job always gives same task
    public static TextTask getTaskForJob(String jobName) {
        if (TASKS.isEmpty()) {
            throw new IllegalStateException("No code tasks");
        }
        if (jobName == null || jobName.trim().equals("")) {
            return TASKS.get(0);
        }
        int hash = jobName.hashCode();
        if (hash < 0) {
            hash = -hash;
        }
        int index = hash % TASKS.size();
        return TASKS.get(index);
    }
}

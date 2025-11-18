package provider;

import java.util.List;

import model.minigame.TextTask;

// Holds the code tasks for the Programmer mini-game.
// Data only: all lookup logic lives in AbstractTextTaskProvider.
public final class CodeTaskProvider extends AbstractTextTaskProvider {

    private static final CodeTaskProvider INSTANCE = new CodeTaskProvider();

    private CodeTaskProvider() {
        super("No code tasks");
    }

    public static CodeTaskProvider get() {
        return INSTANCE;
    }

    @Override
    protected void loadTasks(List<TextTask> into) {
        into.add(new TextTask("hello", "Hello Program",
                "print(\"Hello, client!\")",
                30, 1));
        into.add(new TextTask("sum", "Sum Function",
                "def total(a, b):\n    return a + b\n\nprint(total(21, 21))",
                60, 1));
        into.add(new TextTask("loop", "Counter Loop",
                "for i in range(1, 6):\n    print(f\"Order #{i} ready\")",
                60, 2));
        into.add(new TextTask("max", "Find Maximum",
                "def biggest(a, b):\n    if a > b:\n        return a\n    return b\n\nprint(biggest(17, 42))",
                90, 2));
        into.add(new TextTask("discount", "Discount Calculator",
                "def price(amount, vip):\n    off = 0.2 if vip else 0.1\n    return amount * (1 - off)\n\nprint(price(100, True))",
                110, 3));
    }
}

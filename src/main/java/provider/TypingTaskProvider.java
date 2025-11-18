package provider;

import java.util.List;

import model.minigame.TextTask;

// Holds the typing tasks for the Typist mini-game.
// Data only: all lookup logic lives in AbstractTextTaskProvider.
public final class TypingTaskProvider extends AbstractTextTaskProvider {

    private static final TypingTaskProvider INSTANCE = new TypingTaskProvider();

    private TypingTaskProvider() {
        super("No typing tasks");
    }

    public static TypingTaskProvider get() {
        return INSTANCE;
    }

    @Override
    protected void loadTasks(List<TextTask> into) {
        into.add(new TextTask("note-1", "Office Note",
                "Please file these invoices by noon and confirm the delivery date with the client.",
                45, 1));
        into.add(new TextTask("memo-1", "Team Memo",
                "The weekly meeting moves to Thursday at ten. Bring your progress report and your questions.",
                60, 1));
        into.add(new TextTask("article-1", "Short Article",
                "Good typing is a quiet skill. Steady hands, steady rhythm, and fewer mistakes beat raw speed every time.",
                75, 2));
        into.add(new TextTask("report-1", "Client Report",
                "Revenue grew twelve percent this quarter, driven by repeat customers and faster delivery times across every region.",
                90, 2));
        into.add(new TextTask("contract-1", "Legal Paragraph",
                "The undersigned parties hereby agree to settle all outstanding balances within thirty days of receipt, without exception.",
                110, 3));
    }
}

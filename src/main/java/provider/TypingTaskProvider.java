package provider;

import java.util.ArrayList;
import java.util.List;

import model.minigame.TextTask;

// Holds the typing tasks for the Typist mini-game.
public final class TypingTaskProvider {

    private static ArrayList<TextTask> TASKS = new ArrayList<TextTask>();

    static {
        add(new TextTask("note-1", "Office Note",
                "Please file these invoices by noon and confirm the delivery date with the client.",
                45, 1));
        add(new TextTask("memo-1", "Team Memo",
                "The weekly meeting moves to Thursday at ten. Bring your progress report and your questions.",
                60, 1));
        add(new TextTask("article-1", "Short Article",
                "Good typing is a quiet skill. Steady hands, steady rhythm, and fewer mistakes beat raw speed every time.",
                75, 2));
        add(new TextTask("report-1", "Client Report",
                "Revenue grew twelve percent this quarter, driven by repeat customers and faster delivery times across every region.",
                90, 2));
        add(new TextTask("contract-1", "Legal Paragraph",
                "The undersigned parties hereby agree to settle all outstanding balances within thirty days of receipt, without exception.",
                110, 3));
    }

    private TypingTaskProvider() {
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

    // Pick one task for a job name. Same job always gives same task.
    public static TextTask getTaskForJob(String jobName) {
        if (TASKS.isEmpty()) {
            throw new IllegalStateException("No typing tasks");
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

package provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.minigame.TextTask;

/**
 * Prose passages for the Typist mini-game (Factory/Provider pattern).
 *
 * <p>To add a passage, add one line in the static block. Difficulty 1–3
 * drives the badge and the par time for speed points.</p>
 */
public final class TypingTaskProvider {

    private static final List<TextTask> TASKS = new ArrayList<>();

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
        return Collections.unmodifiableList(TASKS);
    }

    public static TextTask getById(String id) {
        return TASKS.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Deterministic job → challenge binding (single-pick flow).
     * Each job always maps to the same passage so the Workshop's
     * "Available Jobs" pick never opens an unrelated second picker.
     */
    public static TextTask getTaskForJob(String jobName) {
        if (TASKS.isEmpty()) throw new IllegalStateException("No typing tasks");
        if (jobName == null || jobName.isBlank()) return TASKS.get(0);
        return TASKS.get(Math.floorMod(jobName.hashCode(), TASKS.size()));
    }
}

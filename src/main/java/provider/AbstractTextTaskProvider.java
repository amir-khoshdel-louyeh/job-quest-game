package provider;

import java.util.ArrayList;
import java.util.List;

import model.minigame.TextTask;

// Shared lookup logic for TextTask catalogs (Template Method).
// Subclasses only supply data via loadTasks(); the getAll/getById/
// getTaskForJob skeleton lives here once — the old duplication across
// TypingTaskProvider/CodeTaskProvider (including the same
// Integer.MIN_VALUE hash bug twice) is gone.
public abstract class AbstractTextTaskProvider {
    private final ArrayList<TextTask> tasks = new ArrayList<TextTask>();
    private final String emptyMessage;

    protected AbstractTextTaskProvider(String emptyMessage) {
        if (emptyMessage == null) {
            this.emptyMessage = "No tasks";
        } else {
            this.emptyMessage = emptyMessage;
        }
        loadTasks(tasks);
    }

    // Subclasses add their catalog entries here.
    protected abstract void loadTasks(List<TextTask> into);

    public final List<TextTask> getAll() {
        ArrayList<TextTask> copy = new ArrayList<TextTask>();
        for (int i = 0; i < tasks.size(); i++) {
            copy.add(tasks.get(i));
        }
        return copy;
    }

    public final TextTask getById(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < tasks.size(); i++) {
            TextTask t = tasks.get(i);
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    // Pick one task for a job name. Same job always gives same task.
    public final TextTask getTaskForJob(String jobName) {
        if (tasks.isEmpty()) {
            throw new IllegalStateException(emptyMessage);
        }
        if (jobName == null || jobName.trim().equals("")) {
            return tasks.get(0);
        }
        // Old code did "if (hash < 0) hash = -hash", which stays negative
        // for Integer.MIN_VALUE and then yields a negative index.
        // Same mapping as before for every other hash; that one value
        // now maps to index 0 instead of crashing.
        int hash = jobName.hashCode();
        if (hash == Integer.MIN_VALUE) {
            hash = 0;
        } else if (hash < 0) {
            hash = -hash;
        }
        int index = hash % tasks.size();
        return tasks.get(index);
    }
}

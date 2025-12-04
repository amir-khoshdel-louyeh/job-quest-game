package provider;

import java.util.ArrayList;
import java.util.List;

import utils.Lists;

// Shared base for single-list catalogs (jobs, skills, services, achievements).
// A proper template: the base owns storage and the lookup algorithm, while
// every subclass supplies its data and says how to read an entry's name.
public abstract class AbstractCatalog<T> {

    private final ArrayList<T> entries;

    // ----- Construction -----
    AbstractCatalog(List<T> entries) {
        if (entries == null) {
            throw new IllegalArgumentException("entries must not be null");
        }
        this.entries = new ArrayList<T>(entries);
    }

    // ----- Getter -----
    // Returns a copy so callers cannot change the catalog.
    public final List<T> getAll() {
        return Lists.copyOf(entries);
    }

    // ----- Queries -----
    // Template method: the lookup loop lives here once; subclasses only
    // supply the abstract nameOf hook.
    public final T findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < entries.size(); i++) {
            T entry = entries.get(i);
            if (name.equals(nameOf(entry))) {
                return entry;
            }
        }
        return null;
    }

    // ----- Abstract hook -----
    // Every catalog entry type has a display name; the base cannot know it.
    protected abstract String nameOf(T entry);

    // ----- Protected helpers -----
    // A copy to filter over; the stored list is never exposed.
    protected final List<T> snapshot() {
        return Lists.copyOf(entries);
    }
}

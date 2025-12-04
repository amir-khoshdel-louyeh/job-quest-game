package provider;

import java.util.ArrayList;
import java.util.List;

import provider.contract.Catalog;
import utils.Lists;

// Shared base for single-list catalogs (jobs, skills, shop items, achievements,
// challenges, quiz questions).
// A proper template: the base owns storage, the listing algorithm, and name
// lookup, while every subclass supplies its data and says how to read an
// entry's name. Catalogs whose entries are mutable also override copyOf.
public abstract class AbstractCatalog<T> implements Catalog<T> {

    private final ArrayList<T> entries;

    // ----- Construction -----
    AbstractCatalog(List<T> entries) {
        if (entries == null) {
            throw new IllegalArgumentException("entries must not be null");
        }
        this.entries = new ArrayList<T>(entries);
    }

    // ----- Getter -----
    // Returns a copy of the list and, through copyOf, a safe copy of every
    // entry, so callers can never change the catalog.
    @Override
    public final List<T> getAll() {
        ArrayList<T> copy = new ArrayList<T>();
        for (int i = 0; i < entries.size(); i++) {
            copy.add(copyOf(entries.get(i)));
        }
        return copy;
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
                return copyOf(entry);
            }
        }
        return null;
    }

    // ----- Abstract hook -----
    // Every catalog entry type has a display name; the base cannot know it.
    protected abstract String nameOf(T entry);

    // ----- Hook -----
    // Immutable entries are handed out as they are; a catalog holding mutable
    // entries overrides this to clone on every read.
    protected T copyOf(T entry) {
        return entry;
    }

    // ----- Protected helpers -----
    // A copy to filter over; the stored list is never exposed.
    protected final List<T> snapshot() {
        return Lists.copyOf(entries);
    }
}

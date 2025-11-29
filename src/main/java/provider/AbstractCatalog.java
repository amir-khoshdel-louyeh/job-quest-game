package provider;

import java.util.ArrayList;
import java.util.List;

import utils.Lists;

// Shared base for single-list catalogs (jobs, skills, services, achievements).
// Subclasses supply data; read via getAll()/snapshot().
public abstract class AbstractCatalog<T> {

    private final ArrayList<T> entries;

    // ----- Construction -----
    protected AbstractCatalog(List<T> entries) {
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

    // ----- Protected helpers -----
    // A copy to filter over; the stored list is never exposed.
    protected final List<T> snapshot() {
        return Lists.copyOf(entries);
    }
}

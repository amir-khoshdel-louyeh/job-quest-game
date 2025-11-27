package provider;

import java.util.ArrayList;
import java.util.List;

// Shared base for every single-list catalog (jobs, skills, services,
// achievements). Template: subclasses only supply data, the defensive-copy
// mechanics live here once — the old null-check + new ArrayList<>(...)
// + copy-loop trio was duplicated in every provider.
//
// OOP showcase for the defense:
// - abstract class + extends (vs. the interface-only contracts elsewhere),
// - parametric polymorphism: one class works for Job, Service, ...,
// - final methods: getAll()/size() can never be broken by a subclass,
// - protected members: subclasses filter via snapshot(), they never touch
//   the private list directly (encapsulation),
// - protected generic method copyOf(): a second parametric-polymorphism spot.
//
// Why not everything extends this? Different-shaped catalogs
// (the factory registry in IdentityProvider with Map<String, Supplier<Identity>>)
// would break SRP under a single-list base. They reuse catalog behavior
// via composition instead — composition over inheritance applied consciously.
public abstract class AbstractCatalog<T> {

    private final ArrayList<T> entries;

    protected AbstractCatalog(List<T> entries) {
        if (entries == null) {
            throw new IllegalArgumentException("entries must not be null");
        }
        this.entries = new ArrayList<T>(entries);
    }

    // Public read API. Final so no subclass can leak the live list.
    // Returns a copy so callers can't change the catalog from outside.
    public final List<T> getAll() {
        return copyOf(entries);
    }

    public final int size() {
        return entries.size();
    }

    // Subclass hook: a private copy to filter over. Filter methods like
    // getJobsForIdentity / getHealthServices iterate this snapshot and
    // return their own result list — the stored list is never exposed.
    protected final List<T> snapshot() {
        return copyOf(entries);
    }

    // Generic helper: the same code copies List<Job>, List<Service>, ...
    // with compile-time type safety (parametric polymorphism).
    protected static <E> ArrayList<E> copyOf(List<E> source) {
        ArrayList<E> copy = new ArrayList<E>();
        if (source == null) {
            return copy;
        }
        for (int i = 0; i < source.size(); i++) {
            copy.add(source.get(i));
        }
        return copy;
    }
}

package observer;

/**
 * Subject interface for the observer pattern; manages observers and notifies them of changes.
 *
 * <p>OOP Step 5: now a true interface (was a concrete class). Subjects
 * implement this and delegate to {@link SimpleSubject} via composition,
 * keeping their single-inheritance slot free.</p>
 *
 * <p>Follows Single Responsibility Principle by defining subject behaviour only.</p>
 */
public interface Subject {
    /** Register an observer if not already present. */
    void addObserver(Observer observer);

    /** Unregister an observer. */
    void removeObserver(Observer observer);

    /** Notify all observers (pull: they re-read state). */
    void notifyObservers();

    /**
     * Notify with a hint about what changed (push), e.g. "balance", "health".
     * Default fans out to the no-arg version so existing subjects keep working.
     */
    default void notifyObservers(String whatChanged) {
        notifyObservers();
    }

    /** Return number of registered observers. */
    int observerCount();
}
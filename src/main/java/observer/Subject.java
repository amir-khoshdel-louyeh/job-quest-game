package observer;

// Lets watchers sign up and get told when something changes.
// Views depend on Observable; only controllers hold a Subject.
public interface Subject extends Observable {
    void notifyObservers();

    // Tell watchers what changed, like balance or health.
    void notifyObservers(ChangeType whatChanged);
}

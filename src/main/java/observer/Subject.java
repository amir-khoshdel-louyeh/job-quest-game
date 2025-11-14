package observer;

// Lets watchers sign up and get told when something changes.
public interface Subject {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();

    // Tell watchers what changed, like balance or health.
    default void notifyObservers(String whatChanged) {
        notifyObservers();
    }

    int observerCount();
}

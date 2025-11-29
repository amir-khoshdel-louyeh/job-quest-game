package observer;

// The subscribe side of the observer pattern: what views are allowed to do.
// Only the controller (a Subject) may notify.
public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);
}

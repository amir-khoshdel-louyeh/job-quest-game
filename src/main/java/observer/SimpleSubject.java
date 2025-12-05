package observer;

import java.util.ArrayList;
import java.util.List;

import utils.GameLog;
import utils.Require;

// Holds watchers and tells them when something changes.
public final class SimpleSubject implements Subject {
    private final List<Observer> observers = new ArrayList<Observer>();

    // ----- Private helpers -----
    // One shared copy so both notify methods behave the same.
    private List<Observer> snapshot() {
        return new ArrayList<Observer>(observers);
    }

    // ----- Commands -----
    @Override
    public void addObserver(Observer observer) {
        Observer validObserver = Require.notNull(observer, "observer");
        if (!observers.contains(validObserver)) {
            observers.add(validObserver);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(Require.notNull(observer, "observer"));
    }

    @Override
    public void notifyObservers() {
        // Copy first so removing during notify is safe.
        List<Observer> copy = snapshot();
        for (Observer o : copy) {
            try {
                o.update();
            } catch (RuntimeException e) {
                // One faulty observer must not stop the others; runtime
                // failures are all an observer callback can throw.
                GameLog.error("notifying observer", e);
            }
        }
    }

    @Override
    public void notifyObservers(ChangeType whatChanged) {
        // Copy first so removing during notify is safe.
        List<Observer> copy = snapshot();
        for (Observer o : copy) {
            try {
                o.update(whatChanged);
            } catch (RuntimeException e) {
                // One faulty observer must not stop the others; runtime
                // failures are all an observer callback can throw.
                GameLog.error("notifying observer", e);
            }
        }
    }
}

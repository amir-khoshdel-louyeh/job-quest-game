package observer;

import java.util.ArrayList;
import java.util.List;

// Holds watchers and tells them when something changes.
public class SimpleSubject implements Subject {
    private final List<Observer> observers = new ArrayList<Observer>();

    // ----- Private helpers -----
    // One shared copy so both notify methods behave the same.
    private List<Observer> snapshot() {
        return new ArrayList<Observer>(observers);
    }

    // ----- Commands -----
    @Override
    public void addObserver(Observer observer) {
        if (observer == null) {
            return;
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observer == null) {
            return;
        }
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // Copy first so removing during notify is safe.
        List<Observer> copy = snapshot();
        for (Observer o : copy) {
            try {
                o.update();
            } catch (Exception e) {
                utils.GameLog.error("notifying observer", e);
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
            } catch (Exception e) {
                utils.GameLog.error("notifying observer", e);
            }
        }
    }
}

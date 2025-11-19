package observer;

import java.util.ArrayList;
import java.util.List;

// Holds a list of watchers and tells them when something changes.
public class SimpleSubject implements Subject {
    private final List<Observer> observers = new ArrayList<Observer>();

    // One shared copy method so both notify methods do the same thing.
    private List<Observer> snapshot() {
        return new ArrayList<Observer>(observers);
    }

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

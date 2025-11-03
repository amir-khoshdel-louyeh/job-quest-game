package observer;

/**
 * Subject interface for the observer pattern; manages observers and notifies them of changes.
 *
 * Follows Single Responsibility Principle by defining subject behaviour only.
 */

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subject {
    private final List<Observer> observers = new CopyOnWriteArrayList<>();


    public void addObserver(Observer observer) {
        // register an observer if not already present
        if (observer == null) {
            return;
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        // unregister an observer
        if (observer == null) {
            return;
        }
        observers.remove(observer);
    }

    
    public void notifyObservers() {
        // notify all observers, safely handling exceptions
        for (Observer observer : observers) {
            try {
                observer.update();
            } catch (RuntimeException e) {
                System.err.println("Observer threw exception during update: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    
    public int observerCount() {
        // return number of registered observers
        return observers.size();
    }
}
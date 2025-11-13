package observer;

import java.util.ArrayList;
import java.util.List;

// This class holds a list of observers and notifies them.
// It shows Observer pattern: when data changes, GUI updates.
// Uses simple ArrayList (beginner way, no CopyOnWriteArrayList).
public class SimpleSubject implements Subject {
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer observer) {
        // register an observer if not already present
        if (observer == null) {
            return;
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        // unregister an observer
        if (observer == null) {
            return;
        }
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        // Tell all observers, uses simple for loop.
        // Make a copy first so removing during notify is safe.
        ArrayList<Observer> copy = new ArrayList<Observer>();
        for (int i = 0; i < observers.size(); i++) {
            copy.add(observers.get(i));
        }
        for (int i = 0; i < copy.size(); i++) {
            try {
                copy.get(i).update();
            } catch (Exception e) {
                System.out.println("Observer error: " + e.getMessage());
            }
        }
    }

    @Override
    public void notifyObservers(String whatChanged) {
        // Tell all observers what changed.
        ArrayList<Observer> copy = new ArrayList<Observer>();
        for (int i = 0; i < observers.size(); i++) {
            copy.add(observers.get(i));
        }
        for (int i = 0; i < copy.size(); i++) {
            try {
                copy.get(i).update(whatChanged);
            } catch (Exception e) {
                System.out.println("Observer error: " + e.getMessage());
            }
        }
    }

    @Override
    public int observerCount() {
        // return number of registered observers
        return observers.size();
    }
}

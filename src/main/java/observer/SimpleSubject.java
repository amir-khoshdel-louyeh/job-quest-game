package observer;

import java.util.ArrayList;
import java.util.List;

// Holds a list of watchers and tells them when something changes.
public class SimpleSubject implements Subject {
    private ArrayList<Observer> observers = new ArrayList<Observer>();

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
        // Copy first so removing during notify is safe.
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
        return observers.size();
    }
}

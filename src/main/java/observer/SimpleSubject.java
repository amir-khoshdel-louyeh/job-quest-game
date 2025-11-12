package observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * OOP Step 5: reusable composition helper for the {@link Subject} interface.
 *
 * <p>Previously {@code Subject} was a concrete base class, so
 * {@code UserController extends Subject} burned Java's single inheritance
 * slot. Now subjects <em>implement</em> {@link Subject} and <em>hold</em> a
 * {@code SimpleSubject} (composition over inheritance).</p>
 */
public class SimpleSubject implements Subject {
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

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

    @Override
    public void notifyObservers(String whatChanged) {
        // push variant: observers that care about the hint use it,
        // others fall back to pull via update()
        for (Observer observer : observers) {
            try {
                observer.update(whatChanged);
            } catch (RuntimeException e) {
                System.err.println("Observer threw exception during update: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    @Override
    public int observerCount() {
        // return number of registered observers
        return observers.size();
    }
}

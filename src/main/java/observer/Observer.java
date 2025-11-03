package observer;

/**
 * Observer interface for receiving notifications from subjects (observer pattern).
 *
 * Follows Single Responsibility Principle by specifying observer contract only.
 */

public interface Observer {
    // triggered when the observed subject changes
    void update();
}
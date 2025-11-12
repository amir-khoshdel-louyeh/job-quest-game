package observer;

/**
 * Observer interface for receiving notifications from subjects (observer pattern).
 *
 * <p>OOP Step 5: pull ({@link #update()}) stays for backward compatibility;
 * push ({@link #update(String)}) lets subjects hint what changed
 * ("balance", "health", ...) so observers can refresh selectively.</p>
 *
 * <p>Follows Single Responsibility Principle by specifying observer contract only.</p>
 */
public interface Observer {
    // triggered when the observed subject changes
    void update();

    /**
     * Push variant with a change hint. Default delegates to pull
     * so existing observers keep working untouched.
     */
    default void update(String whatChanged) {
        update();
    }
}
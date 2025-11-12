package controller;

import model.Item;
import model.User;
import observer.SimpleSubject;
import observer.Subject;
/**
 * Manages the state and actions of a single user.
 * This class acts as a buffer between the raw User model and the rest of the application,
 * providing safe and centralized methods to modify user data.
 *
 * <p>OOP Step 5: implements {@link Subject} via composition
 * (holds a {@link SimpleSubject}) instead of {@code extends Subject},
 * so the single-inheritance slot stays free.</p>
 */
public class UserController implements Subject {
    private final User user;
    private final SimpleSubject subject = new SimpleSubject();

    public UserController(User user) {
    // create user controller wrapping a User model
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        this.user = user;
    }

    // ---- Subject delegation (composition) ----
    @Override
    public void addObserver(observer.Observer observer) { subject.addObserver(observer); }

    @Override
    public void removeObserver(observer.Observer observer) { subject.removeObserver(observer); }

    @Override
    public void notifyObservers() { subject.notifyObservers(); }

    @Override
    public void notifyObservers(String whatChanged) { subject.notifyObservers(whatChanged); }

    @Override
    public int observerCount() { return subject.observerCount(); }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public int getBalance() {
        return user.getBalance();
    }

    public int getHealth() {
        return user.getHealth();
    }

    public int getEnergy() {
        return user.getEnergy();
    }

    public void addBalance(int amount) {
        if (amount > 0) {
            user.deposit(amount);
            notifyObservers("balance");
        }
    }

    public boolean deductBalance(int amount) {
    // try to deduct amount from user's balance (no notify)
        if (amount > 0 && user.getBalance() >= amount) {
            user.withdraw(amount);
            return true; // The calling method is responsible for notifying observers.
            // Note: Observers are not notified here to allow for transactional operations.
        }
        return false;
    }

    public void increaseHealth(int amount) {
        if (amount > 0) {
            user.gainHealth(amount);
            notifyObservers("health");
        }
    }

    public void decreaseHealth(int amount) {
        if (amount > 0) {
            user.loseHealth(amount);
            notifyObservers("health");
        }
    }

    public void increaseEnergy(int amount) {
        if (amount > 0) {
            user.gainEnergy(amount);
            notifyObservers("energy");
        }
    }

    public void decreaseEnergy(int amount) {
        if (amount > 0) {
            user.loseEnergy(amount);
            notifyObservers("energy");
        }
    }

    public boolean isBlocked() {
        return user.isBlocked();
    }

    public long getBlockedUntil() {
        return user.getBlockedUntil();
    }

    public void blockUser(long durationMillis) {
    // block the user for the given duration
        user.setBlockedUntil(System.currentTimeMillis() + durationMillis);
    }

    public void unblockUser() {
    // remove any block on the user
        user.setBlockedUntil(0);
    }

    public void addItemToInventory(Item item) {
        user.getInventory().addItem(item);
        // The calling controller is responsible for notifying observers.
    }

    /**
     * OOP Step 4: encapsulated skill learning — the only sanctioned way to
     * mutate skills via the controller (notifies observers). Callers must
     * not use {@code getUser().getSkills().add(...)} (unmodifiable now).
     * @return true when newly learned
     */
    public boolean addSkill(model.Skill skill) {
        if (user.addSkill(skill)) {
            notifyObservers("skills");
            return true;
        }
        return false;
    }

    // ---------------- Progression (Encapsulation + Observer) ----------------
    // Delegates keep User fields encapsulated: outside code must go through
    // here so every mutation notifies observers consistently.

    /** Return the wrapped User model (escape hatch for read-only providers). */
    // Note: prefer the delegates below for mutations.

    public int getLevel() {
        return user.getLevel();
    }

    /** Add XP (may trigger level-up inside User) and notify observers. */
    public void addExperience(int amount) {
        if (amount > 0) {
            user.addExperience(amount);
            notifyObservers("xp");
        }
    }

    /** Add reputation (clamped 0-100 in User) and notify observers. */
    public void addReputation(int amount) {
        if (amount != 0) {
            user.addReputation(amount);
            notifyObservers("reputation");
        }
    }

    /** Record one completed job and lifetime earnings. */
    public void recordJobCompletion(int moneyEarned) {
        user.incrementJobsCompleted();
        if (moneyEarned > 0) {
            user.addToTotalMoneyEarned(moneyEarned);
        }
        notifyObservers("jobs");
    }

    /**
     * Re-notify observers after domain Singleton services mutate the
     * wrapped {@link User} directly (e.g. challenge/achievement rewards).
     * Keeps Observer pattern consistent without breaking service APIs.
     */
    public void refresh() {
        notifyObservers();
    }
}
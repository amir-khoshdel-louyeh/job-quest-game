package controller;

import model.Item;
import model.User;
import observer.Subject;
/**
 * Manages the state and actions of a single user.
 * This class acts as a buffer between the raw User model and the rest of the application,
 * providing safe and centralized methods to modify user data.
 */
public class UserController extends Subject {
    private User user;

    public UserController(User user) {
    // create user controller wrapping a User model
        this.user = user;
    }

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
            notifyObservers();
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
            notifyObservers();
        }
    }

    public void decreaseHealth(int amount) {
        if (amount > 0) {
            user.loseHealth(amount);
            notifyObservers();
        }
    }

    public void increaseEnergy(int amount) {
        if (amount > 0) {
            user.gainEnergy(amount);
            notifyObservers();
        }
    }

    public void decreaseEnergy(int amount) {
        if (amount > 0) {
            user.loseEnergy(amount);
            notifyObservers();
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

    // ---------------- Progression (Encapsulation + Observer) ----------------
    // Delegates keep User fields encapsulated: outside code must go through
    // here so every mutation notifies observers consistently.

    /** Return the wrapped User model (escape hatch for read-only providers). */
    // Note: prefer the delegates below for mutations.

    public int getLevel() {
        return user.getLevel();
    }

    public int getExperience() {
        return user.getExperience();
    }

    public int getReputation() {
        return user.getReputation();
    }

    public int getTotalJobsCompleted() {
        return user.getTotalJobsCompleted();
    }

    public int getTotalMoneyEarned() {
        return user.getTotalMoneyEarned();
    }

    /** Add XP (may trigger level-up inside User) and notify observers. */
    public void addExperience(int amount) {
        if (amount > 0) {
            user.addExperience(amount);
            notifyObservers();
        }
    }

    /** Add reputation (clamped 0-100 in User) and notify observers. */
    public void addReputation(int amount) {
        if (amount != 0) {
            user.addReputation(amount);
            notifyObservers();
        }
    }

    /** Record one completed job and lifetime earnings. */
    public void recordJobCompletion(int moneyEarned) {
        user.incrementJobsCompleted();
        if (moneyEarned > 0) {
            user.addToTotalMoneyEarned(moneyEarned);
        }
        notifyObservers();
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
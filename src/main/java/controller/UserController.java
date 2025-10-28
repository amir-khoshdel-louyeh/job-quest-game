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
        user.setBlockedUntil(System.currentTimeMillis() + durationMillis);
    }

    public void unblockUser() {
        user.setBlockedUntil(0);
    }

    public void addItemToInventory(Item item) {
        user.getInventory().addItem(item);
        // The calling controller is responsible for notifying observers.
    }
}
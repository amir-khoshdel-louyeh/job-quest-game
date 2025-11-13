package controller;

import model.Skill;
import model.User;
import observer.Observer;
import observer.SimpleSubject;
import observer.Subject;

// Holds one player and handles balance, health, energy, and XP changes.
public class UserController implements Subject {
    private final User user;
    private final SimpleSubject subject = new SimpleSubject();

    public UserController(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        this.user = user;
    }

    @Override
    public void addObserver(Observer observer) { subject.addObserver(observer); }

    @Override
    public void removeObserver(Observer observer) { subject.removeObserver(observer); }

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
        // No screen refresh here; the caller refreshes once the whole action is done.
        if (amount > 0 && user.getBalance() >= amount) {
            user.withdraw(amount);
            return true;
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
        user.setBlockedUntil(System.currentTimeMillis() + durationMillis);
    }

    public void unblockUser() {
        user.setBlockedUntil(0);
    }

    // Adds a new skill. Returns true if the skill is new.
    public boolean addSkill(Skill skill) {
        if (user.addSkill(skill)) {
            notifyObservers("skills");
            return true;
        }
        return false;
    }

    /** Return the wrapped User model (escape hatch for read-only providers). */
    public int getLevel() {
        return user.getLevel();
    }

    /** Adds XP (can level the player up). */
    public void addExperience(int amount) {
        if (amount > 0) {
            user.addExperience(amount);
            notifyObservers("xp");
        }
    }

    /** Adds reputation (stays between 0-100). */
    public void addReputation(int amount) {
        if (amount != 0) {
            user.addReputation(amount);
            notifyObservers("reputation");
        }
    }

    // Adds one completed job to the player's totals.
    public void recordJobCompletion(int moneyEarned) {
        user.incrementJobsCompleted();
        if (moneyEarned > 0) {
            user.addToTotalMoneyEarned(moneyEarned);
        }
        notifyObservers("jobs");
    }

    public void refresh() {
        notifyObservers();
    }
}
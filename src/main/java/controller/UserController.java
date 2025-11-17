package controller;

import java.util.List;

import model.Identity;
import model.player.User;
import observer.ChangeType;
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
    public void notifyObservers(ChangeType whatChanged) { subject.notifyObservers(whatChanged); }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    // Law-of-Demeter delegates: callers ask the controller instead of
    // digging via getUser().getIdentity()/getSkills()/getLastSicknessTime().
    public String getIdentityName() {
        return user.getIdentityName();
    }

    public Identity getIdentity() {
        return user.getIdentity();
    }

    public List<String> getSkills() {
        return user.getSkills();
    }

    public boolean hasSkill(String skillName) {
        return user.hasSkill(skillName);
    }

    public long getLastSicknessTime() {
        return user.getLastSicknessTime();
    }

    public void markSicknessChecked(long now) {
        user.setLastSicknessTime(now);
    }

    public void addPlayTime(long deltaMillis) {
        user.addPlayTime(deltaMillis);
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
            notifyObservers(ChangeType.BALANCE);
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
            notifyObservers(ChangeType.HEALTH);
        }
    }

    public void decreaseHealth(int amount) {
        if (amount > 0) {
            user.loseHealth(amount);
            notifyObservers(ChangeType.HEALTH);
        }
    }

    public void increaseEnergy(int amount) {
        if (amount > 0) {
            user.gainEnergy(amount);
            notifyObservers(ChangeType.ENERGY);
        }
    }

    public void decreaseEnergy(int amount) {
        if (amount > 0) {
            user.loseEnergy(amount);
            notifyObservers(ChangeType.ENERGY);
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
    public boolean addSkill(String skillName) {
        if (user.addSkill(skillName)) {
            notifyObservers(ChangeType.SKILLS);
            return true;
        }
        return false;
    }

    /** Return the wrapped User model (escape hatch for read-only providers). */
    public int getLevel() {
        return user.getLevel();
    }

    /** Adds XP (level-up refill happens inside User). */
    public void addExperience(int amount) {
        if (amount > 0) {
            user.addExperience(amount);
            notifyObservers(ChangeType.XP);
        }
    }

    /** Adds reputation (stays between 0-100). */
    public void addReputation(int amount) {
        if (amount != 0) {
            user.addReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Adds one completed job to the player's totals.
    // 10-hour run tuning: +1 jobBonus every 10 jobs (capped at 15 for +30%),
    // so the job bonus is reachable in a single run.
    public void recordJobCompletion(int moneyEarned) {
        user.incrementJobsCompleted();
        if (moneyEarned > 0) {
            user.addToTotalMoneyEarned(moneyEarned);
        }
        if (user.getTotalJobsCompleted() % 10 == 0) {
            // Cap lives inside User.setJobBonus (0..15); controller just asks.
            int next = user.getJobBonus() + 1;
            user.setJobBonus(next);
        }
        notifyObservers(ChangeType.JOBS);
    }

    public void refresh() {
        notifyObservers();
    }
}
package controller;

import java.util.List;

import model.Identity;
import model.goals.Challenge;
import model.player.User;
import observer.ChangeType;
import observer.Observer;
import observer.SimpleSubject;
import observer.Subject;

// Holds one player and handles balance, health, energy, and XP changes.
// Views read via the delegates below, never the live User.
public class UserController implements Subject, model.goals.AchievementStats {
    private final User user;
    private final SimpleSubject subject = new SimpleSubject();

    // ----- Construction -----
    public UserController(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        this.user = user;
    }

    // ----- Observer delegates -----
    @Override
    public void addObserver(Observer observer) { subject.addObserver(observer); }

    @Override
    public void removeObserver(Observer observer) { subject.removeObserver(observer); }

    @Override
    public void notifyObservers() { subject.notifyObservers(); }

    @Override
    public void notifyObservers(ChangeType whatChanged) { subject.notifyObservers(whatChanged); }

    // ----- Getter -----
    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    // ----- Identity delegates -----
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

    public model.career.SkillSet getSkillSet() {
        return user.getSkillSet();
    }

    // ----- Read delegates -----
    public int getLevel() {
        return user.getLevel();
    }

    public int getExperience() {
        return user.getExperience();
    }

    public int getRequiredExperienceForNextLevel() {
        return user.getRequiredExperienceForNextLevel();
    }

    public int getExperiencePercentage() {
        return user.getExperiencePercentage();
    }

    public int getReputation() {
        return user.getReputation();
    }

    public long getTotalPlayTime() {
        return user.getTotalPlayTime();
    }

    @Override
    public int getTotalJobsCompleted() {
        return user.getTotalJobsCompleted();
    }

    @Override
    public int getTotalMoneyEarned() {
        return user.getTotalMoneyEarned();
    }

    public int getJobBonus() {
        return user.getJobBonus();
    }

    @Override
    public int getSkillCount() {
        return user.getSkillCount();
    }

    @Override
    public boolean hasAchievement(String id) {
        return user.hasAchievement(id);
    }

    public List<Challenge> getActiveChallenges() {
        return user.getActiveChallenges();
    }

    public long getLastSicknessTime() {
        return user.getLastSicknessTime();
    }

    public void markSicknessChecked(long now) {
        user.markSicknessChecked(now);
    }

    // ----- Commands -----
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
        user.blockUntil(System.currentTimeMillis() + durationMillis);
    }

    public void unblockUser() {
        user.unblock();
    }

    // Adds a new skill. Returns true if the skill is new.
    public boolean addSkill(String skillName) {
        if (user.addSkill(skillName)) {
            notifyObservers(ChangeType.SKILLS);
            return true;
        }
        return false;
    }

    // Adds XP.
    public void addExperience(int amount) {
        if (amount > 0) {
            user.addExperience(amount);
            notifyObservers(ChangeType.XP);
        }
    }

    // Gains reputation.
    public void gainReputation(int amount) {
        if (amount > 0) {
            user.gainReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Loses reputation.
    public void loseReputation(int amount) {
        if (amount > 0) {
            user.loseReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Records one completed job and its pay.
    public void recordJobCompletion(int moneyEarned) {
        user.addCompletedJob();
        if (moneyEarned > 0) {
            user.earn(moneyEarned);
            notifyObservers(ChangeType.BALANCE);
        }
        notifyObservers(ChangeType.JOBS);
    }

    public void refresh() {
        notifyObservers();
    }
}
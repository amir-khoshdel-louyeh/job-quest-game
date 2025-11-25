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
// Implements AchievementStats by delegation so views pass this controller
// where stats are needed — never the live User (Law of Demeter).
public class UserController implements Subject, model.goals.AchievementStats {
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

    // Internal escape hatch for services (Challenge/Achievement/Event/Job):
    // domain logic works on User. Views must use the delegates below instead.
    // (Kept to avoid a bigger service-layer refactor in this step.)

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

    // Query-only skill view for career rules (no list digging, no manual loops).
    public model.career.SkillSet getSkillSet() {
        return user.getSkillSet();
    }

    // Read delegates: views ask here instead of digging via getUser().
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
    // Single atomic transaction: balance + lifetime total move together
    // via User.earn, so callers make ONE call instead of addBalance + addTotal.
    public void recordJobCompletion(int moneyEarned) {
        user.incrementJobsCompleted();
        if (moneyEarned > 0) {
            user.earn(moneyEarned);
            notifyObservers(ChangeType.BALANCE);
        }
        if (user.getTotalJobsCompleted() % 10 == 0) {
            // Domain behavior: cap lives inside User (0..15).
            user.grantJobBonusIncrement();
        }
        notifyObservers(ChangeType.JOBS);
    }

    public void refresh() {
        notifyObservers();
    }

    // ----- Demo scenario -----
    public void setBalanceForDemo(int amount) {
        user.debugSetBalance(amount);
        notifyObservers(ChangeType.BALANCE);
    }

    public void setHealthForDemo(int hp) {
        user.debugSetHealth(hp);
        notifyObservers(ChangeType.HEALTH);
    }

    public void setEnergyForDemo(int energy) {
        user.debugSetEnergy(energy);
        notifyObservers(ChangeType.ENERGY);
    }

    public void setLevelForDemo(int level) {
        user.debugSetLevel(level);
        notifyObservers(ChangeType.XP);
    }

    public void setReputationForDemo(int reputation) {
        user.debugSetReputation(reputation);
        notifyObservers(ChangeType.REPUTATION);
    }

    public void setJobBonusForDemo(int jobBonus) {
        user.debugSetJobBonus(jobBonus);
        notifyObservers();
    }
}
package model.player;

import java.util.List;

import model.Identity;
import model.goals.AchievementBook;
import model.goals.AchievementStats;
import model.goals.Challenge;
import model.goals.ChallengeBook;
import model.store.ServiceTarget;

// Represents one player. Thin aggregate root: owns Wallet/Vitals/Progress
// plus small book objects (skills, blocking, achievements, challenges).
// Owns its aggregate invariant: level-up always refills health/energy here,
// so callers never need a separate restoreFull() call.
// Implements AchievementStats + ServiceTarget so goals/store depend on
// small interfaces (DIP), not on this concrete class.
public class User implements AchievementStats, ServiceTarget {
    private String username;
    private Identity identity;
    private Wallet wallet;
    private Vitals vitals;
    private CareerProgress progress;
    private SkillBook skillBook = new SkillBook();
    private BlockStatus blockStatus = new BlockStatus();
    private AchievementBook achievementBook = new AchievementBook();
    private ChallengeBook challengeBook = new ChallengeBook();

    private long totalPlayTime = 0;

    private int totalJobsCompleted = 0;
    private int jobBonus = 0;

    public User(String username, Identity identity, int balance) {
        this.username = username;
        this.identity = identity;
        this.wallet = new Wallet(balance);
        this.vitals = new Vitals();
        this.progress = new CareerProgress();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Identity getIdentity() {
        return identity;
    }

    // Convenience for views: keeps the getIdentity().getDisplayName()
    // chain inside the aggregate so callers obey the Law of Demeter.
    public String getIdentityName() {
        if (identity == null) {
            return "?";
        }
        return identity.getDisplayName();
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public int getBalance() {
        return wallet.getBalance();
    }

    public void deposit(int amount) {
        wallet.deposit(amount);
    }

    public void withdraw(int amount) {
        wallet.withdraw(amount);
    }

    // Exact setter. Clamping (>= 0) lives inside Wallet,
    // so callers just say "set to X" (Tell-Don't-Ask).
    public void setBalance(int amount) {
        wallet.setBalance(amount);
    }

    public int getHealth() {
        return vitals.getHealth();
    }

    // Exact setter. Clamping (0..MAX_HEALTH) lives inside Vitals.
    public void setHealth(int hp) {
        vitals.setHealth(hp);
    }

    @Override
    public void gainHealth(int amount) {
        vitals.gainHealth(amount);
    }

    public void loseHealth(int amount) {
        vitals.loseHealth(amount);
    }

    public int getEnergy() {
        return vitals.getEnergy();
    }

    // Exact setter. Clamping (0..MAX_ENERGY) lives inside Vitals.
    public void setEnergy(int energy) {
        vitals.setEnergy(energy);
    }

    @Override
    public void gainEnergy(int amount) {
        vitals.gainEnergy(amount);
    }

    public void loseEnergy(int amount) {
        vitals.loseEnergy(amount);
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<String> getSkills() {
        return skillBook.getSkills();
    }

    // Adds the skill unless it is missing or already learned.
    public boolean addSkill(String skillName) {
        return skillBook.addSkill(skillName);
    }

    @Override
    public int getSkillCount() {
        return skillBook.getSkills().size();
    }

    public boolean hasSkill(String skillName) {
        return skillBook.hasSkill(skillName);
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    // Rich behavior: play time only moves forward. Negative deltas are ignored,
    // so callers cannot rewind or forge the clock with a wholesale setter.
    public void addPlayTime(long deltaMillis) {
        if (deltaMillis > 0) {
            this.totalPlayTime += deltaMillis;
        }
    }

    public void setBlockedUntil(long time) {
        blockStatus.setBlockedUntil(time);
    }

    public long getBlockedUntil() {
        return blockStatus.getBlockedUntil();
    }

    // Blocked while the current time is before the stored time.
    public boolean isBlocked() {
        return blockStatus.isBlocked();
    }

    public void setLastSicknessTime(long time) {
        blockStatus.setLastSicknessTime(time);
    }

    public long getLastSicknessTime() {
        return blockStatus.getLastSicknessTime();
    }

    public int getLevel() {
        return progress.getLevel();
    }

    public void setLevel(int level) {
        progress.setLevel(level);
    }

    public int getExperience() {
        return progress.getExperience();
    }

    // Returns true on level up. Refills health/energy on level up (aggregate invariant).
    public boolean setExperience(int experience) {
        boolean leveledUp = progress.setExperience(experience);
        if (leveledUp) {
            vitals.restoreFull();
        }
        return leveledUp;
    }

    // Returns true on level up. Refills health/energy on level up (aggregate invariant).
    public boolean addExperience(int amount) {
        boolean leveledUp = progress.addExperience(amount);
        if (leveledUp) {
            vitals.restoreFull();
        }
        return leveledUp;
    }

    public void restoreFull() {
        vitals.restoreFull();
    }

    public int getRequiredExperienceForNextLevel() {
        return progress.getRequiredExperienceForNextLevel();
    }

    public int getExperiencePercentage() {
        return progress.getExperiencePercentage();
    }

    public int getReputation() {
        return progress.getReputation();
    }

    public void setReputation(int reputation) {
        progress.setReputation(reputation);
    }

    public void addReputation(int amount) {
        progress.addReputation(amount);
    }

    public int getTotalMoneyEarned() {
        return wallet.getTotalMoneyEarned();
    }

    public void addToTotalMoneyEarned(int amount) {
        wallet.addToTotalMoneyEarned(amount);
    }

    public int getTotalJobsCompleted() {
        return totalJobsCompleted;
    }

    public void incrementJobsCompleted() {
        this.totalJobsCompleted++;
    }

    public int getJobBonus() {
        return jobBonus;
    }

    // Job bonus is guarded inside the domain (0..15) so callers
    // cannot forge it with a raw setter. +1 per 10 jobs, max +30%.
    public void setJobBonus(int jobBonus) {
        if (jobBonus < 0) {
            jobBonus = 0;
        }
        if (jobBonus > 15) {
            jobBonus = 15;
        }
        this.jobBonus = jobBonus;
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<String> getUnlockedAchievements() {
        return achievementBook.getUnlockedAchievements();
    }

    public void unlockAchievement(String id) {
        achievementBook.unlockAchievement(id);
    }

    public boolean hasAchievement(String id) {
        return achievementBook.hasAchievement(id);
    }

    // Returns a copy so the stored list can't be changed from outside.
    public List<Challenge> getActiveChallenges() {
        return challengeBook.getActiveChallenges();
    }

    public void addChallenge(Challenge challenge) {
        challengeBook.addChallenge(challenge);
    }

    public void addChallenges(List<Challenge> list) {
        challengeBook.addChallenges(list);
    }

    public boolean hasChallenge(String challengeId) {
        return challengeBook.hasChallenge(challengeId);
    }

    // Removes finished daily challenges.
    public void pruneCompletedDailyChallenges() {
        challengeBook.pruneCompletedDailyChallenges();
    }

    public void removeChallenge(Challenge challenge) {
        if (challenge != null) {
            challengeBook.removeChallengeById(challenge.getId());
        }
    }
}

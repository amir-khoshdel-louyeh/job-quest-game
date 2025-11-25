package model.player;

import java.util.List;
import java.util.Objects;

import model.Identity;
import model.goals.AchievementBook;
import model.goals.AchievementStats;
import model.goals.Challenge;
import model.goals.ChallengeBook;
import model.store.ServiceTarget;

// Represents one player. Thin aggregate root: owns Wallet/Vitals/Progress
// plus small book objects (skills, blocking, achievements, challenges).
// Owns its aggregate invariant: level-up always refills health/energy here,
// Implements AchievementStats + ServiceTarget so goals/store depend on
public class User implements AchievementStats, ServiceTarget {
    private final String username;
    private final Identity identity;
    private final Wallet wallet;
    private final Vitals vitals;
    private final CareerProgress progress;
    private final SkillBook skillBook = new SkillBook();
    private final BlockStatus blockStatus = new BlockStatus();
    private final JobRecord jobRecord = new JobRecord();
    private final AchievementBook achievementBook = new AchievementBook();
    private final ChallengeBook challengeBook = new ChallengeBook();

    private long totalPlayTime = 0;

    // ----- Construction -----
    public User(String username, Identity identity, int balance) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username required");
        }
        this.username = username;
        this.identity = Objects.requireNonNull(identity, "identity must not be null");
        this.wallet = new Wallet(balance);
        this.vitals = new Vitals();
        this.progress = new CareerProgress();
    }

    // ----- Identity Delegate -----
    public String getUsername() {
        return username;
    }

    public Identity getIdentity() {
        return identity;
    }

    public String getIdentityName() {
        if (identity == null) {
            return "?";
        }
        return identity.getDisplayName();
    }

    // ----- Wallet Delegate -----
    public int getBalance() {
        return wallet.getBalance();
    }

    public void deposit(int amount) {
        wallet.deposit(amount);
    }

    public void withdraw(int amount) {
        wallet.withdraw(amount);
    }

    public void earn(int amount) {
        wallet.earn(amount);
    }

    public int getTotalMoneyEarned() {
        return wallet.getTotalMoneyEarned();
    }

    // ----- Vitals Delegate -----
    public int getHealth() {
        return vitals.getHealth();
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

    @Override
    public void gainEnergy(int amount) {
        vitals.gainEnergy(amount);
    }

    public void loseEnergy(int amount) {
        vitals.loseEnergy(amount);
    }

    // ----- SkillBook Delegate -----
    public List<String> getSkills() {
        return skillBook.getSkills();
    }

    public boolean addSkill(String skillName) {
        return skillBook.addSkill(skillName);
    }

    @Override
    public int getSkillCount() {
        return skillBook.getSkillCount();
    }

    public boolean hasSkill(String skillName) {
        return skillBook.hasSkill(skillName);
    }

    public model.career.SkillSet getSkillSet() {
        return skillBook;
    }

    // ----- Play time Delegate -----
    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    // ----- BlockStatus Delegate -----
    public void blockUntil(long time) {
        blockStatus.blockUntil(time);
    }

    public void unblock() {
        blockStatus.unblock();
    }

    public void markSicknessChecked(long time) {
        blockStatus.markSicknessChecked(time);
    }

    public long getBlockedUntil() {
        return blockStatus.getBlockedUntil();
    }

    public boolean isBlocked() {
        return blockStatus.isBlocked();
    }

    public long getLastSicknessTime() {
        return blockStatus.getLastSicknessTime();
    }

    // ----- CareerProgress Delegate -----
    public int getLevel() {
        return progress.getLevel();
    }

    public int getExperience() {
        return progress.getExperience();
    }

    public boolean addExperience(int amount) {
        boolean leveledUp = progress.addExperience(amount);
        if (leveledUp) {
            vitals.restoreFull();
        }
        return leveledUp;
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

    public void gainReputation(int amount) {
        progress.gainReputation(amount);
    }

    public void loseReputation(int amount) {
        progress.loseReputation(amount);
    }

    // ----- Jobs Delegate -----
    public int getTotalJobsCompleted() {
        return jobRecord.getTotalJobsCompleted();
    }

    public void addCompletedJob() {
        jobRecord.addCompletedJob();
    }

    public int getJobBonus() {
        return jobRecord.getJobBonus();
    }

    public void grantStartingBonus() {
        jobRecord.grantStartingBonus();
    }

    // ----- AchievementBook Delegate -----
    public List<String> getUnlockedAchievements() {
        return achievementBook.getUnlockedAchievements();
    }

    public void unlockAchievement(String id) {
        achievementBook.unlockAchievement(id);
    }

    public boolean hasAchievement(String id) {
        return achievementBook.hasAchievement(id);
    }

    // ----- ChallengeBook Delegate -----
    public List<Challenge> getActiveChallenges() {
        return challengeBook.getActiveChallenges();
    }

    public List<Challenge> updateChallengeProgress(Challenge.ChallengeType type, int amount) {
        return challengeBook.updateProgress(type, amount);
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

    public void pruneCompletedDailyChallenges() {
        challengeBook.pruneCompletedDailyChallenges();
    }

    // ----- Cheat harness access -----
    Wallet wallet() { return wallet; }
    Vitals vitals() { return vitals; }
    CareerProgress progress() { return progress; }
    JobRecord jobRecord() { return jobRecord; }
    void addPlayTimeForCheat(long deltaMillis) {
        if (deltaMillis > 0) {
            this.totalPlayTime += deltaMillis;
        }
    }

    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(username, other.username)
                && Objects.equals(identity, other.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, identity);
    }

    @Override
    public String toString() {
        return "User [" + username + " (" + getIdentityName() + ")"
                + ", level " + getLevel()
                + ", balance $" + getBalance()
                + ", health " + getHealth()
                + ", energy " + getEnergy() + "]";
    }
}

package model.player;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import model.Identity;
import model.career.SkillSet;
import model.goals.AchievementBook;
import model.goals.Challenge;
import model.goals.ChallengeBook;
import model.store.ServiceTarget;

// Represents one player. Owns Wallet/Vitals/Progress plus skill,
// blocking, achievement, and challenge books.
// Read/mutated through UserController; services see narrow ports instead.
// Raw components are private: the demo panel uses the DevControls port.
public final class User implements PlayerProgress, ServiceTarget, PaymentStats, UpkeepTarget {
    private final String username;
    private final Identity identity;
    private final Wallet wallet;
    private final Vitals vitals;
    private final CareerProgress progress;
    private final SkillBook skillBook = new SkillBook();
    private final BlockStatus blockStatus;
    private final JobRecord jobRecord = new JobRecord();
    private final AchievementBook achievementBook = new AchievementBook();
    private final ChallengeBook challengeBook = new ChallengeBook();
    private final PlayClock playClock = new PlayClock();
    private final DevControls devControls;

    // ----- Construction -----
    public User(String username, Identity identity, int balance) {
        this(username, identity, balance, System::currentTimeMillis);
    }

    // Fixed clock for tests; the block check uses it instead of the wall clock.
    public User(String username, Identity identity, int balance, Supplier<Long> clock) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username required");
        }
        this.username = username;
        this.identity = Objects.requireNonNull(identity, "identity must not be null");
        this.wallet = new Wallet(balance);
        this.vitals = new Vitals();
        this.progress = new CareerProgress();
        this.blockStatus = new BlockStatus(clock);
        this.devControls = new DemoMutations();
    }

    // ----- Identity Delegate -----
    public String getUsername() {
        return username;
    }

    public Identity getIdentity() {
        return identity;
    }

    public String getIdentityName() {
        return identity.getDisplayName();
    }

    // ----- Wallet Delegate -----
    public int getBalance() {
        return wallet.getBalance();
    }

    public boolean deposit(int amount) {
        return wallet.deposit(amount);
    }

    public boolean withdraw(int amount) {
        return wallet.withdraw(amount);
    }

    public boolean earn(int amount) {
        return wallet.earn(amount);
    }

    public int getTotalMoneyEarned() {
        return wallet.getTotalMoneyEarned();
    }

    // ----- Vitals Delegate -----
    public int getHealth() {
        return vitals.getHealth();
    }

    public int getHealthPercentage() {
        return vitals.getHealthPercentage();
    }

    @Override
    public boolean gainHealth(int amount) {
        return vitals.gainHealth(amount);
    }

    public boolean loseHealth(int amount) {
        return vitals.loseHealth(amount);
    }

    public int getEnergy() {
        return vitals.getEnergy();
    }

    public int getEnergyPercentage() {
        return vitals.getEnergyPercentage();
    }

    @Override
    public boolean gainEnergy(int amount) {
        return vitals.gainEnergy(amount);
    }

    public boolean loseEnergy(int amount) {
        return vitals.loseEnergy(amount);
    }

    // ----- SkillBook Delegate -----
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

    public SkillSet getSkillSet() {
        return skillBook;
    }

    // ----- Play time Delegate -----
    public long getPlayTimeMinutes() {
        return playClock.getTotalMinutes();
    }

    public void addPlayTime(long millis) {
        playClock.addMillis(millis);
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

    // Adds XP; refills health/energy on level-up.
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
        return challengeBook.updateChallengeProgress(type, amount);
    }

    public void addChallenges(List<Challenge> list) {
        challengeBook.addChallenges(list);
    }

    public boolean hasChallenge(String challengeId) {
        return challengeBook.hasChallenge(challengeId);
    }

    // ----- DevControls (demo panel) -----
    // Returns the narrow demo port, never the internal components. User itself
    // is only reachable from the controller layer, so views cannot use this.
    public DevControls devControls() {
        return devControls;
    }

    // Exact-value demo mutations, kept out of the public User surface.
    private final class DemoMutations implements DevControls {
        @Override
        public void setBalance(int amount) {
            wallet.setBalanceForCheat(amount);
        }

        @Override
        public void setHealth(int health) {
            vitals.setHealthForCheat(health);
        }

        @Override
        public void setEnergy(int energy) {
            vitals.setEnergyForCheat(energy);
        }

        @Override
        public void setLevel(int level) {
            progress.setLevelForCheat(level);
        }

        @Override
        public void setReputation(int reputation) {
            progress.setReputationForCheat(reputation);
        }

        @Override
        public void setJobBonus(int jobBonus) {
            jobRecord.setJobBonusForCheat(jobBonus);
        }

        @Override
        public void addPlayTimeMinutes(long minutes) {
            playClock.addMillis(minutes * utils.DurationText.MILLIS_PER_MINUTE);
        }

        @Override
        public void setPlayTimeMinutes(long minutes) {
            playClock.setMinutesForCheat(minutes);
        }
    }

    // ----- Achievement summary -----
    public int getAchievementCount() {
        return achievementBook.getUnlockedCount();
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

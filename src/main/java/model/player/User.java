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

    public int getBalance() {
        return wallet.getBalance();
    }

    public void deposit(int amount) {
        wallet.deposit(amount);
    }

    public void withdraw(int amount) {
        wallet.withdraw(amount);
    }

    // Atomic job income: delegates to Wallet.earn so balance and
    // lifetime total can never diverge (no temporal coupling).
    public void earn(int amount) {
        wallet.earn(amount);
    }

    // Demo/debug only: exact balance override for the Developer panel.
    // Game logic must use deposit/withdraw/earn instead.
    public void debugSetBalance(int amount) {
        wallet.debugSetBalance(amount);
    }

    public int getHealth() {
        return vitals.getHealth();
    }

    // Demo/debug only: exact health override for the Developer panel.
    // Game logic must use gainHealth/loseHealth/restoreFull instead.
    public void debugSetHealth(int hp) {
        vitals.debugSetHealth(hp);
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

    // Demo/debug only: exact energy override for the Developer panel.
    // Game logic must use gainEnergy/loseEnergy/restoreFull instead.
    public void debugSetEnergy(int energy) {
        vitals.debugSetEnergy(energy);
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

    // Query-only view (no mutators): safe to share — callers ask behavior,
    // they never receive the mutable list or book itself.
    public model.career.SkillSet getSkillSet() {
        return skillBook;
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

    // Demo/debug only: exact level override for the Developer panel.
    // Game logic must use addExperience instead.
    public void debugSetLevel(int level) {
        progress.debugSetLevel(level);
    }

    public int getExperience() {
        return progress.getExperience();
    }

    // Returns true on level up. Refills health/energy on level up (aggregate invariant).
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

    // Demo/debug only: exact reputation override for the Developer panel.
    // Game logic must use addReputation instead.
    public void debugSetReputation(int reputation) {
        progress.debugSetReputation(reputation);
    }

    public void addReputation(int amount) {
        progress.addReputation(amount);
    }

    public int getTotalMoneyEarned() {
        return wallet.getTotalMoneyEarned();
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
    // Private: game logic uses increment/record paths; demos use debug below.
    private void setJobBonus(int jobBonus) {
        if (jobBonus < 0) {
            jobBonus = 0;
        }
        if (jobBonus > 15) {
            jobBonus = 15;
        }
        this.jobBonus = jobBonus;
    }

    // New-account welcome: grants the first bonus point through domain rules.
    // StartPanel uses this instead of a raw setter (no forgery path for game logic).
    public void grantStartingBonus() {
        if (this.jobBonus == 0) {
            setJobBonus(1);
        }
    }

    // Domain behavior: +1 bonus point (capped 0..15 inside). Used every 10 jobs
    // so controllers never do get-then-set forgery.
    public void grantJobBonusIncrement() {
        setJobBonus(this.jobBonus + 1);
    }

    // Demo/debug only: exact bonus override for the Developer panel.
    public void debugSetJobBonus(int jobBonus) {
        setJobBonus(jobBonus);
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

    // Returns deep copies so the stored progress can't be changed from outside.
    public List<Challenge> getActiveChallenges() {
        return challengeBook.getActiveChallenges();
    }

    // Rich behavior: progress mutation lives inside the aggregate.
    // Returns snapshots of newly completed challenges (for rewards/display).
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

    // Removes finished daily challenges.
    public void pruneCompletedDailyChallenges() {
        challengeBook.pruneCompletedDailyChallenges();
    }
}

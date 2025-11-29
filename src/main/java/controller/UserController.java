package controller;

import java.util.List;

import model.Identity;
import model.career.SkillSet;
import model.goals.Challenge;
import model.player.DevControls;
import model.player.JobPlayer;
import model.player.PlayerView;
import model.player.PurchaseTarget;
import model.player.SkillLearner;
import model.player.UpkeepTarget;
import model.player.User;
import observer.ChangeType;
import observer.Observable;
import observer.Observer;
import observer.SimpleSubject;
import observer.Subject;

// Adapter around one player: commands plus observer notifications.
// Commands are implemented on this class; reads and subscriptions are handed
// out as two separate projection objects, so a view cannot downcast what it
// receives back to a command port, to Subject, or to the mutable aggregate.
final class UserController implements Subject, JobPlayer,
        SkillLearner, PurchaseTarget, UpkeepTarget {
    private final User user;
    private final SimpleSubject subject = new SimpleSubject();
    private final PlayerView playerView = new ReadOnlyPlayer();
    private final Observable observable = new SubscriptionPort();

    // ----- Construction -----
    public UserController(User user) {
        this.user = utils.Require.notNull(user, "user");
    }

    // ----- Read-only projections -----
    // The object screens receive as their player data. It implements
    // PlayerView and nothing else.
    PlayerView playerView() {
        return playerView;
    }

    // The object screens receive to subscribe. It implements Observable and
    // nothing else, so a view cannot call notifyObservers().
    Observable observable() {
        return observable;
    }

    // ----- Observer delegates -----
    @Override
    public void addObserver(Observer observer) {
        subject.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        subject.removeObserver(observer);
    }

    @Override
    public void notifyObservers() {
        subject.notifyObservers();
    }

    @Override
    public void notifyObservers(ChangeType whatChanged) {
        subject.notifyObservers(whatChanged);
    }

    // ----- Dev access -----
    // The demo panel mutates through the narrow port; no other layer sees User.
    DevControls devControls() {
        return user.devControls();
    }

    // ----- Reads required by the command ports -----
    @Override
    public Identity getIdentity() {
        return user.getIdentity();
    }

    @Override
    public SkillSet getSkillSet() {
        return user.getSkillSet();
    }

    @Override
    public int getLevel() {
        return user.getLevel();
    }

    @Override
    public int getReputation() {
        return user.getReputation();
    }

    @Override
    public int getJobBonus() {
        return user.getJobBonus();
    }

    @Override
    public long getPlayTimeMinutes() {
        return user.getPlayTimeMinutes();
    }

    // Adds elapsed play time; the upkeep scheduler calls this once per minute.
    public void addPlayTime(long millis) {
        if (millis > 0) {
            user.addPlayTime(millis);
        }
    }

    @Override
    public int getHealth() {
        return user.getHealth();
    }

    @Override
    public int getEnergy() {
        return user.getEnergy();
    }

    @Override
    public int getBalance() {
        return user.getBalance();
    }

    @Override
    public boolean isBlocked() {
        return user.isBlocked();
    }

    @Override
    public int getTotalJobsCompleted() {
        return user.getTotalJobsCompleted();
    }

    @Override
    public int getTotalMoneyEarned() {
        return user.getTotalMoneyEarned();
    }

    @Override
    public int getSkillCount() {
        return user.getSkillCount();
    }

    @Override
    public boolean hasAchievement(String id) {
        return user.hasAchievement(id);
    }

    // ----- Money / vitals commands -----
    @Override
    public boolean deductBalance(int amount) {
        if (user.withdraw(amount)) {
            notifyObservers(ChangeType.BALANCE);
            return true;
        }
        return false;
    }

    // Adds money (reward, challenge, or achievement).
    @Override
    public boolean deposit(int amount) {
        if (user.deposit(amount)) {
            notifyObservers(ChangeType.BALANCE);
            return true;
        }
        return false;
    }

    // Adds health and tells observers; Service.apply uses this surface.
    @Override
    public boolean gainHealth(int amount) {
        if (user.gainHealth(amount)) {
            notifyObservers(ChangeType.HEALTH);
            return true;
        }
        return false;
    }

    // Adds energy and tells observers; Service.apply uses this surface.
    @Override
    public boolean gainEnergy(int amount) {
        if (user.gainEnergy(amount)) {
            notifyObservers(ChangeType.ENERGY);
            return true;
        }
        return false;
    }

    @Override
    public boolean loseEnergy(int amount) {
        if (user.loseEnergy(amount)) {
            notifyObservers(ChangeType.ENERGY);
            return true;
        }
        return false;
    }

    @Override
    public boolean loseHealth(int amount) {
        if (user.loseHealth(amount)) {
            notifyObservers(ChangeType.HEALTH);
            return true;
        }
        return false;
    }

    // Upkeep uses the same money path as every other withdrawal.
    @Override
    public boolean withdraw(int amount) {
        return deductBalance(amount);
    }

    // ----- Block -----
    @Override
    public void blockUntil(long time) {
        user.blockUntil(time);
    }

    @Override
    public void markSicknessChecked(long time) {
        user.markSicknessChecked(time);
    }

    @Override
    public long getLastSicknessTime() {
        return user.getLastSicknessTime();
    }

    public void unblockUser() {
        user.unblock();
    }

    // ----- Skills / progress -----
    // Adds a new skill. Returns true if the skill is new.
    @Override
    public boolean addSkill(String skillName) {
        if (user.addSkill(skillName)) {
            notifyObservers(ChangeType.SKILLS);
            return true;
        }
        return false;
    }

    // Adds XP. A level-up refills vitals inside User, so health and energy
    // observers must be told too or the bars stay stale.
    @Override
    public boolean addExperience(int amount) {
        if (amount <= 0) {
            return false;
        }
        boolean leveledUp = user.addExperience(amount);
        notifyObservers(ChangeType.XP);
        if (leveledUp) {
            notifyObservers(ChangeType.HEALTH);
            notifyObservers(ChangeType.ENERGY);
        }
        return leveledUp;
    }

    // Gains reputation.
    @Override
    public void gainReputation(int amount) {
        if (amount > 0) {
            user.gainReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Loses reputation.
    @Override
    public void loseReputation(int amount) {
        if (amount > 0) {
            user.loseReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Records one completed job and its pay.
    @Override
    public void recordJobCompletion(int moneyEarned) {
        user.addCompletedJob();
        if (moneyEarned > 0) {
            user.earn(moneyEarned);
            notifyObservers(ChangeType.BALANCE);
        }
        notifyObservers(ChangeType.JOBS);
    }

    // ----- Challenge / achievement ports -----
    @Override
    public List<Challenge> getActiveChallenges() {
        return user.getActiveChallenges();
    }

    @Override
    public List<Challenge> updateChallengeProgress(Challenge.ChallengeType type, int amount) {
        return user.updateChallengeProgress(type, amount);
    }

    @Override
    public void addChallenges(List<Challenge> challenges) {
        user.addChallenges(challenges);
    }

    @Override
    public void unlockAchievement(String id) {
        user.unlockAchievement(id);
    }

    // ----- Full refresh -----
    // Demo and play-time changes have no dedicated ChangeType; callers that
    // touch several stats at once use this instead of guessing a subset.
    public void publishAll() {
        notifyObservers();
    }

    // ----- Nested projections -----
    // Read-only player data for screens. It deliberately implements nothing
    // else, so it cannot be cast to a command port.
    private final class ReadOnlyPlayer implements PlayerView {

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public Identity getIdentity() {
            return user.getIdentity();
        }

        @Override
        public String getIdentityName() {
            return user.getIdentityName();
        }

        @Override
        public SkillSet getSkillSet() {
            return user.getSkillSet();
        }

        @Override
        public int getSkillCount() {
            return user.getSkillCount();
        }

        @Override
        public int getLevel() {
            return user.getLevel();
        }

        @Override
        public int getExperience() {
            return user.getExperience();
        }

        @Override
        public int getRequiredExperienceForNextLevel() {
            return user.getRequiredExperienceForNextLevel();
        }

        @Override
        public int getExperiencePercentage() {
            return user.getExperiencePercentage();
        }

        @Override
        public int getReputation() {
            return user.getReputation();
        }

        @Override
        public int getJobBonus() {
            return user.getJobBonus();
        }

        @Override
        public long getPlayTimeMinutes() {
            return user.getPlayTimeMinutes();
        }

        @Override
        public int getHealth() {
            return user.getHealth();
        }

        @Override
        public int getHealthPercentage() {
            return user.getHealthPercentage();
        }

        @Override
        public int getEnergy() {
            return user.getEnergy();
        }

        @Override
        public int getEnergyPercentage() {
            return user.getEnergyPercentage();
        }

        @Override
        public int getBalance() {
            return user.getBalance();
        }

        @Override
        public boolean isBlocked() {
            return user.isBlocked();
        }

        @Override
        public long getBlockedUntil() {
            return user.getBlockedUntil();
        }

        @Override
        public int getAchievementCount() {
            return user.getAchievementCount();
        }

        @Override
        public int getTotalJobsCompleted() {
            return user.getTotalJobsCompleted();
        }

        @Override
        public int getTotalMoneyEarned() {
            return user.getTotalMoneyEarned();
        }

        @Override
        public boolean hasAchievement(String id) {
            return user.hasAchievement(id);
        }
    }

    // Subscribe-only view of the subject. It deliberately implements nothing
    // else, so a view cannot cast it to Subject and call notifyObservers().
    private final class SubscriptionPort implements Observable {

        @Override
        public void addObserver(Observer observer) {
            UserController.this.addObserver(observer);
        }

        @Override
        public void removeObserver(Observer observer) {
            UserController.this.removeObserver(observer);
        }
    }
}

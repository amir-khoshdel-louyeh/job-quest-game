package controller;

import java.util.EnumSet;
import java.util.List;

import model.Identity;
import model.career.SkillSet;
import model.goals.Challenge;
import model.player.DevControls;
import model.player.JobPlayer;
import model.player.PaymentStats;
import model.player.PurchaseTarget;
import model.player.SkillLearner;
import model.player.PlayerProgress;
import model.player.UpkeepTarget;
import model.player.User;
import model.store.ServiceTarget;
import observer.ChangeType;
import observer.Observer;
import observer.SimpleSubject;
import observer.Subject;

// Holds one player and handles commands plus observer notifications.
// Reads live in PlayerReadModel; views see this object only as Observable.
final class UserController implements Subject, PlayerProgress,
        PaymentStats, ServiceTarget, UpkeepTarget,
        JobPlayer, SkillLearner, PurchaseTarget {
    private final User user;
    private final SimpleSubject subject = new SimpleSubject();
    private int batchDepth;
    private boolean pendingFullRefresh;
    private final EnumSet<ChangeType> pendingChanges = EnumSet.noneOf(ChangeType.class);

    // ----- Construction -----
    public UserController(User user) {
        this.user = utils.Require.notNull(user, "user");
    }

    // ----- Observer delegates -----
    @Override
    public void addObserver(Observer observer) { subject.addObserver(observer); }

    @Override
    public void removeObserver(Observer observer) { subject.removeObserver(observer); }

    @Override
    public void notifyObservers() {
        if (batchDepth > 0) {
            pendingFullRefresh = true;
            return;
        }
        subject.notifyObservers();
    }

    @Override
    public void notifyObservers(ChangeType whatChanged) {
        if (batchDepth > 0) {
            if (whatChanged == null) {
                pendingFullRefresh = true;
            } else {
                pendingChanges.add(whatChanged);
            }
            return;
        }
        subject.notifyObservers(whatChanged);
    }

    // ----- Batching -----
    // Hold notifications until the whole action finishes, then publish once.
    @Override
    public void runBatch(Runnable action) {
        utils.Require.notNull(action, "action");
        batchDepth++;
        try {
            action.run();
        } finally {
            batchDepth--;
            if (batchDepth == 0) {
                flushBatch();
            }
        }
    }

    // One full refresh when several parts changed; one targeted update otherwise.
    private void flushBatch() {
        boolean fullRefresh = pendingFullRefresh;
        EnumSet<ChangeType> changes = EnumSet.copyOf(pendingChanges);
        pendingFullRefresh = false;
        pendingChanges.clear();
        if (fullRefresh || changes.size() > 1) {
            subject.notifyObservers();
        } else if (!changes.isEmpty()) {
            subject.notifyObservers(changes.iterator().next());
        }
    }

    // ----- Dev access -----
    // The demo panel mutates through the narrow port; no other layer sees User.
    DevControls devControls() {
        return user.devControls();
    }

    // ----- Domain read ports -----
    // Only reads used by commands/services stay here; view reads moved out.
    public Identity getIdentity() {
        return user.getIdentity();
    }

    public SkillSet getSkillSet() {
        return user.getSkillSet();
    }

    public int getLevel() {
        return user.getLevel();
    }

    public int getReputation() {
        return user.getReputation();
    }

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

    public void decreaseEnergy(int amount) {
        loseEnergy(amount);
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

    public boolean isBlocked() {
        return user.isBlocked();
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
    public boolean hasChallenge(String challengeId) {
        return user.hasChallenge(challengeId);
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
}
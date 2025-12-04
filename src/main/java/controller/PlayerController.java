package controller;

import java.util.List;

import model.career.Profession;
import model.career.SkillSet;
import model.goals.Challenge;
import model.goals.ChallengeType;
import model.player.JobPlayer;
import model.player.PlayerView;
import model.player.PurchaseTarget;
import model.player.SkillLearner;
import model.player.UpkeepTarget;
import model.player.Player;
import observer.ChangeType;
import observer.Observable;
import observer.Observer;
import observer.ReadOnlyObservable;
import observer.SimpleSubject;
import observer.Subject;
import utils.Require;

// Adapter around one player: commands plus observer notifications.
// Commands are implemented on this class; reads and subscriptions are handed
// out as two separate projection objects, so a view cannot downcast what it
// receives back to a command port, to Subject, or to the mutable aggregate.
final class PlayerController implements Subject, JobPlayer,
        SkillLearner, PurchaseTarget, UpkeepTarget {
    private final Player player;
    private final SimpleSubject subject = new SimpleSubject();
    private final PlayerView playerView;
    private final Observable observable;

    // ----- Construction -----
    PlayerController(Player player) {
        this.player = Require.notNull(player, "player");
        this.playerView = new ReadOnlyPlayerView(player);
        this.observable = new ReadOnlyObservable(subject);
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

    // ----- Reads required by the command ports -----
    @Override
    public Profession getProfession() {
        return player.getProfession();
    }

    @Override
    public SkillSet getSkillSet() {
        return player.getSkillSet();
    }

    @Override
    public int getLevel() {
        return player.getLevel();
    }

    @Override
    public int getReputation() {
        return player.getReputation();
    }

    @Override
    public int getJobBonus() {
        return player.getJobBonus();
    }

    @Override
    public long getPlayTimeMinutes() {
        return player.getPlayTimeMinutes();
    }

    // Adds elapsed play time; the upkeep scheduler calls this once per minute.
    @Override
    public void addPlayTime(long millis) {
        if (millis > 0) {
            player.addPlayTime(millis);
        }
    }

    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public int getEnergy() {
        return player.getEnergy();
    }

    @Override
    public int getBalance() {
        return player.getBalance();
    }

    @Override
    public boolean isBlocked() {
        return player.isBlocked();
    }

    @Override
    public int getTotalJobsCompleted() {
        return player.getTotalJobsCompleted();
    }

    @Override
    public int getTotalMoneyEarned() {
        return player.getTotalMoneyEarned();
    }

    @Override
    public int getSkillCount() {
        return player.getSkillCount();
    }

    @Override
    public boolean hasAchievement(String id) {
        return player.hasAchievement(id);
    }

    // ----- Money / vitals commands -----
    @Override
    public boolean deductBalance(int amount) {
        if (player.withdraw(amount)) {
            notifyObservers(ChangeType.BALANCE);
            return true;
        }
        return false;
    }

    // Adds money (reward, challenge, or achievement).
    @Override
    public boolean deposit(int amount) {
        if (player.deposit(amount)) {
            notifyObservers(ChangeType.BALANCE);
            return true;
        }
        return false;
    }

    // Adds health and tells observers; ShopItem.apply uses this surface.
    @Override
    public boolean gainHealth(int amount) {
        if (player.gainHealth(amount)) {
            notifyObservers(ChangeType.HEALTH);
            return true;
        }
        return false;
    }

    // Adds energy and tells observers; ShopItem.apply uses this surface.
    @Override
    public boolean gainEnergy(int amount) {
        if (player.gainEnergy(amount)) {
            notifyObservers(ChangeType.ENERGY);
            return true;
        }
        return false;
    }

    @Override
    public boolean loseEnergy(int amount) {
        if (player.loseEnergy(amount)) {
            notifyObservers(ChangeType.ENERGY);
            return true;
        }
        return false;
    }

    @Override
    public boolean loseHealth(int amount) {
        if (player.loseHealth(amount)) {
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
        player.blockUntil(time);
    }

    @Override
    public void markSicknessChecked(long time) {
        player.markSicknessChecked(time);
    }

    @Override
    public long getLastSicknessTime() {
        return player.getLastSicknessTime();
    }

    // ----- Skills / progress -----
    // Adds a new skill. Returns true if the skill is new.
    @Override
    public boolean addSkill(String skillName) {
        if (player.addSkill(skillName)) {
            notifyObservers(ChangeType.SKILLS);
            return true;
        }
        return false;
    }

    // Adds XP. A level-up refills vitals inside Player, so health and energy
    // observers must be told too or the bars stay stale.
    @Override
    public boolean addExperience(int amount) {
        if (amount <= 0) {
            return false;
        }
        boolean leveledUp = player.addExperience(amount);
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
            player.gainReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Loses reputation.
    @Override
    public void loseReputation(int amount) {
        if (amount > 0) {
            player.loseReputation(amount);
            notifyObservers(ChangeType.REPUTATION);
        }
    }

    // Records one completed job and its pay.
    @Override
    public void recordJobCompletion(int moneyEarned) {
        player.addCompletedJob();
        if (moneyEarned > 0) {
            player.earn(moneyEarned);
            notifyObservers(ChangeType.BALANCE);
        }
        notifyObservers(ChangeType.JOBS);
    }

    // ----- Challenge / achievement ports -----
    @Override
    public List<Challenge> getActiveChallenges() {
        return player.getActiveChallenges();
    }

    @Override
    public List<Challenge> updateChallengeProgress(ChallengeType type, int amount) {
        return player.updateChallengeProgress(type, amount);
    }

    @Override
    public void addChallenges(List<Challenge> challenges) {
        player.addChallenges(challenges);
    }

    @Override
    public void unlockAchievement(String id) {
        player.unlockAchievement(id);
    }
}

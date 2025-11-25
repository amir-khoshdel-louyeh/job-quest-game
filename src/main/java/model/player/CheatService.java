package model.player;

// Demo/cheat harness for the Developer panel and tests.
//
// Lives in model.player (not services) on purpose: the cheat setters on
// Wallet/Vitals/CareerProgress/JobRecord are package-private, so ONLY this
// class can reach them. User itself has zero cheat API; normal game code
// in other packages cannot cheat even by accident.
//
// Layering: DeveloperController -> CheatService -> model. The service
// mutates silently; the controller owns observer notifications.
public class CheatService {

    /** Set balance to an exact value (clamped >= 0 inside Wallet). Demo only. */
    public void setBalance(User user, int amount) {
        requireUser(user);
        user.wallet().setBalanceForCheat(amount);
    }

    /** Set health to an exact value (clamped 0..MAX_HEALTH inside Vitals). Demo only. */
    public void setHealth(User user, int hp) {
        requireUser(user);
        user.vitals().setHealthForCheat(hp);
    }

    /** Set energy to an exact value (clamped 0..MAX_ENERGY inside Vitals). Demo only. */
    public void setEnergy(User user, int energy) {
        requireUser(user);
        user.vitals().setEnergyForCheat(energy);
    }

    /** Set level directly (clamped 1..MAX_LEVEL inside CareerProgress). Demo only. */
    public void setLevel(User user, int level) {
        requireUser(user);
        user.progress().setLevelForCheat(level);
    }

    /** Set reputation directly (clamped 0..MAX_REPUTATION). Demo only. */
    public void setReputation(User user, int reputation) {
        requireUser(user);
        user.progress().setReputationForCheat(reputation);
    }

    /** Set job bonus directly (clamped 0..MAX_JOB_BONUS). Demo only. */
    public void setJobBonus(User user, int jobBonus) {
        requireUser(user);
        user.jobRecord().setJobBonusForCheat(jobBonus);
    }

    /**
     * Add playtime (e.g. drives skill unlock gates). Non-positive deltas
     * are ignored: play time never rewinds (use a dedicated reset if
     * rewinding is ever needed).
     */
    public void addPlayTime(User user, long deltaMillis) {
        requireUser(user);
        user.addPlayTimeForCheat(deltaMillis);
    }

    /** Add playtime in whole minutes. Non-positive values are ignored. */
    public void addPlaytimeMinutes(User user, long minutes) {
        requireUser(user);
        if (minutes <= 0) {
            return;
        }
        user.addPlayTimeForCheat(minutes * 60 * 1000);
    }

    private void requireUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
    }
}

package model.player;

// Demo/cheat harness for the Developer panel and tests.
// Lives here so it can reach the package-private cheat setters.

public class CheatService {

    // ----- Commands -----
    // Sets balance to an exact value. Demo only.
    public void setBalance(User user, int amount) {
        requireUser(user);
        user.wallet().setBalanceForCheat(amount);
    }

    // Sets health to an exact value. Demo only.
    public void setHealth(User user, int hp) {
        requireUser(user);
        user.vitals().setHealthForCheat(hp);
    }

    // Sets energy to an exact value. Demo only.
    public void setEnergy(User user, int energy) {
        requireUser(user);
        user.vitals().setEnergyForCheat(energy);
    }

    // Sets level directly. Demo only.
    public void setLevel(User user, int level) {
        requireUser(user);
        user.progress().setLevelForCheat(level);
    }

    // Sets reputation directly. Demo only.
    public void setReputation(User user, int reputation) {
        requireUser(user);
        user.progress().setReputationForCheat(reputation);
    }

    // Sets job bonus directly. Demo only.
    public void setJobBonus(User user, int jobBonus) {
        requireUser(user);
        user.jobRecord().setJobBonusForCheat(jobBonus);
    }

    // Adds playtime; non-positive deltas are ignored.
    public void addPlayTime(User user, long deltaMillis) {
        requireUser(user);
        user.addPlayTimeForCheat(deltaMillis);
    }

    // Adds playtime in whole minutes; non-positive values are ignored.
    public void addPlaytimeMinutes(User user, long minutes) {
        requireUser(user);
        if (minutes <= 0) {
            return;
        }
        user.addPlayTimeForCheat(minutes * 60 * 1000);
    }

    // ----- Private helpers -----
    private void requireUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
    }
}

package controller;

// Lets developers set stats by hand for demos and testing.
public class DeveloperController {
    private final UserController userController;

    public DeveloperController(UserController userController) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
    }

    // No getUser(): views read via the delegates below, never the live
    // User — the last mutable escape hatch is closed (Demeter).

    // Read delegates: views ask here instead of digging via getUser().
    public int getHealth() {
        return userController.getHealth();
    }

    public int getEnergy() {
        return userController.getEnergy();
    }

    public int getLevel() {
        return userController.getLevel();
    }

    public int getReputation() {
        return userController.getReputation();
    }

    public long getTotalPlayTime() {
        return userController.getTotalPlayTime();
    }

    public int getJobBonus() {
        return userController.getJobBonus();
    }

    public int getBalance() {
        return userController.getBalance();
    }

    /** Set balance to an exact value (clamped >= 0 inside Wallet). Demo only. */
    public void setBalance(int amount) {
        userController.setBalanceForDemo(amount);
    }

    /** Set health to an exact value (clamped 0..MAX_HEALTH inside Vitals). Demo only. */
    public void setHealth(int hp) {
        userController.setHealthForDemo(hp);
    }

    /** Set energy to an exact value (clamped 0..MAX_ENERGY inside Vitals). Demo only. */
    public void setEnergy(int energy) {
        userController.setEnergyForDemo(energy);
    }

    /** Add (or remove, when negative) playtime — drives skill unlock gates. */
    public void addPlaytimeMinutes(long minutes) {
        if (minutes == 0) return;
        if (minutes > 0) {
            userController.addPlayTime(minutes * 60 * 1000);
        }
        // Negative minutes intentionally ignored: play time never rewinds
        // (encapsulation — use a dedicated reset if rewinding is ever needed).
        userController.refresh();
    }

    /** Set level directly (clamped 1..MAX_LEVEL inside CareerProgress). Demo only. */
    public void setLevel(int level) {
        userController.setLevelForDemo(level);
    }

    /** Set reputation directly (clamped 0-100 inside User). Demo only. */
    public void setReputation(int reputation) {
        userController.setReputationForDemo(reputation);
    }

    /** Set job bonus level directly (clamped 0..15 inside User). Demo only. */
    public void setJobBonus(int level) {
        userController.setJobBonusForDemo(level);
    }

    /** Lift any active block (e.g. after demoing the 0-health/0-energy state). */
    public void unblock() {
        userController.unblockUser();
        userController.refresh();
    }
}

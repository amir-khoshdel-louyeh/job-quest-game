package controller;

import model.player.CheatService;
import observer.ChangeType;

// Lets developers set stats by hand for demos and testing.
// All out-of-rules mutations live in CheatService (service -> model);
// this controller only orchestrates: cheat + notify observers.
// Views read via the delegates below, never the live User (Demeter).
public class DeveloperController {
    private final UserController userController;
    private final CheatService cheats;

    public DeveloperController(UserController userController) {
        this(userController, new CheatService());
    }

    // Injection seam: pass a shared/stub CheatService in tests.
    public DeveloperController(UserController userController, CheatService cheats) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        if (cheats == null) {
            throw new IllegalArgumentException("cheats must not be null");
        }
        this.userController = userController;
        this.cheats = cheats;
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
        cheats.setBalance(userController.getUser(), amount);
        userController.notifyObservers(ChangeType.BALANCE);
    }

    /** Set health to an exact value (clamped 0..MAX_HEALTH inside Vitals). Demo only. */
    public void setHealth(int hp) {
        cheats.setHealth(userController.getUser(), hp);
        userController.notifyObservers(ChangeType.HEALTH);
    }

    /** Set energy to an exact value (clamped 0..MAX_ENERGY inside Vitals). Demo only. */
    public void setEnergy(int energy) {
        cheats.setEnergy(userController.getUser(), energy);
        userController.notifyObservers(ChangeType.ENERGY);
    }

    /** Add (or remove, when negative) playtime — drives skill unlock gates. */
    public void addPlaytimeMinutes(long minutes) {
        if (minutes == 0) return;
        cheats.addPlaytimeMinutes(userController.getUser(), minutes);
        // Negative minutes intentionally ignored inside CheatService:
        // play time never rewinds (use a dedicated reset if needed).
        userController.refresh();
    }

    /** Set level directly (clamped 1..MAX_LEVEL inside CareerProgress). Demo only. */
    public void setLevel(int level) {
        cheats.setLevel(userController.getUser(), level);
        userController.notifyObservers(ChangeType.XP);
    }

    /** Set reputation directly (clamped 0-100 inside User). Demo only. */
    public void setReputation(int reputation) {
        cheats.setReputation(userController.getUser(), reputation);
        userController.notifyObservers(ChangeType.REPUTATION);
    }

    /** Set job bonus level directly (clamped 0..15 inside User). Demo only. */
    public void setJobBonus(int level) {
        cheats.setJobBonus(userController.getUser(), level);
        userController.notifyObservers();
    }

    /** Lift any active block (e.g. after demoing the 0-health/0-energy state). */
    public void unblock() {
        userController.unblockUser();
        userController.refresh();
    }
}

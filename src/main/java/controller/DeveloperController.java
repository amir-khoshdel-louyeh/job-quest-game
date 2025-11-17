package controller;

import model.player.User;

// Lets developers set stats by hand for demos and testing.
public class DeveloperController {
    private final UserController userController;

    public DeveloperController(UserController userController) {
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        this.userController = userController;
    }

    public User getUser() {
        return userController.getUser();
    }

    /** Set balance to an exact value (clamped >= 0). */
    public void setBalance(int amount) {
        User user = userController.getUser();
        int diff = Math.max(0, amount) - user.getBalance();
        if (diff > 0) {
            user.deposit(diff);
        } else if (diff < 0) {
            user.withdraw(-diff);
        }
        userController.refresh();
    }

    /** Set health to an exact value (clamped 0-100). */
    public void setHealth(int hp) {
        User user = userController.getUser();
        int diff = Math.max(0, Math.min(100, hp)) - user.getHealth();
        if (diff > 0) {
            user.gainHealth(diff);
        } else if (diff < 0) {
            user.loseHealth(-diff);
        }
        userController.refresh();
    }

    /** Set energy to an exact value (clamped 0-100000). */
    public void setEnergy(int energy) {
        User user = userController.getUser();
        int diff = Math.max(0, Math.min(100_000, energy)) - user.getEnergy();
        if (diff > 0) {
            user.gainEnergy(diff);
        } else if (diff < 0) {
            user.loseEnergy(-diff);
        }
        userController.refresh();
    }

    /** Add (or remove, when negative) playtime — drives skill unlock gates. */
    public void addPlaytimeMinutes(long minutes) {
        if (minutes == 0) return;
        User user = userController.getUser();
        if (minutes > 0) {
            user.addPlayTime(minutes * 60 * 1000);
        }
        // Negative minutes intentionally ignored: play time never rewinds
        // (encapsulation — use a dedicated reset if rewinding is ever needed).
        userController.refresh();
    }

    /** Set level directly (clamped 1-100). */
    public void setLevel(int level) {
        userController.getUser().setLevel(Math.max(1, Math.min(100, level)));
        userController.refresh();
    }

    /** Set reputation directly (clamped 0-100 inside User). */
    public void setReputation(int reputation) {
        userController.getUser().setReputation(reputation);
        userController.refresh();
    }

    /** Set job bonus level directly (clamped 0..15 inside User). */
    public void setJobBonus(int level) {
        userController.getUser().setJobBonus(level);
        userController.refresh();
    }

    /** Lift any active block (e.g. after demoing the 0-health/0-energy state). */
    public void unblock() {
        userController.unblockUser();
        userController.refresh();
    }
}

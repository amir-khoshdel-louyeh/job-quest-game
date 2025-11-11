package controller;

import model.User;

/**
 * Demo/dev controls for live presentations and manual testing.
 *
 * Follows Single Responsibility Principle by containing only test-drive
 * state tweaks — game rules stay in the domain controllers. Every mutation
 * goes through the public {@link User} API and ends with
 * {@link UserController#refresh()} so Observer HUDs stay in sync.
 */
public class DeveloperController {
    private final UserController userController;

    public DeveloperController(UserController userController) {
        // construct dev controller wrapping the active user controller
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
        user.setTotalPlayTime(Math.max(0, user.getTotalPlayTime() + minutes * 60 * 1000));
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

    /** Set daily login streak directly (clamped >= 0). */
    public void setStreak(int days) {
        userController.getUser().setCurrentStreak(Math.max(0, days));
        userController.refresh();
    }

    /** Lift any active block (e.g. after demoing the 0-health/0-energy state). */
    public void unblock() {
        userController.unblockUser();
        userController.refresh();
    }
}

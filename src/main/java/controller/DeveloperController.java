package controller;

import model.player.CheatService;
import observer.ChangeType;

// Lets developers set stats by hand for demos and testing.
// Out-of-rules mutations live in CheatService; this only cheats + notifies.
public class DeveloperController {
    private final UserController userController;
    private final CheatService cheats;

    // ----- Construction -----
    public DeveloperController(UserController userController) {
        this(userController, new CheatService());
    }

    // Custom CheatService for tests.
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

    // ----- Read delegates -----
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

    // ----- Commands -----
    // Sets balance. Demo only.
    public void setBalance(int amount) {
        cheats.setBalance(userController.getUser(), amount);
        userController.notifyObservers(ChangeType.BALANCE);
    }

    // Sets health. Demo only.
    public void setHealth(int hp) {
        cheats.setHealth(userController.getUser(), hp);
        userController.notifyObservers(ChangeType.HEALTH);
    }

    // Sets energy. Demo only.
    public void setEnergy(int energy) {
        cheats.setEnergy(userController.getUser(), energy);
        userController.notifyObservers(ChangeType.ENERGY);
    }

    // Adds playtime in minutes; non-positive values are ignored.
    public void addPlaytimeMinutes(long minutes) {
        if (minutes == 0) return;
        cheats.addPlaytimeMinutes(userController.getUser(), minutes);
        userController.refresh();
    }

    // Sets level. Demo only.
    public void setLevel(int level) {
        cheats.setLevel(userController.getUser(), level);
        userController.notifyObservers(ChangeType.XP);
    }

    // Sets reputation. Demo only.
    public void setReputation(int reputation) {
        cheats.setReputation(userController.getUser(), reputation);
        userController.notifyObservers(ChangeType.REPUTATION);
    }

    // Sets job bonus. Demo only.
    public void setJobBonus(int level) {
        cheats.setJobBonus(userController.getUser(), level);
        userController.notifyObservers();
    }

    // Lifts any active block. Demo only.
    public void unblock() {
        userController.unblockUser();
        userController.refresh();
    }
}

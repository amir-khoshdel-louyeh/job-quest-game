package controller;

import observer.ChangeType;

// Lets developers set stats by hand for demos and testing.
// Out-of-rules mutations live in CheatService; this only cheats + notifies.
// The aggregate is reached through UserController.devControls(), not User.
public final class DeveloperController {
    private final UserController userController;
    private final CheatService cheats;

    // ----- Construction -----
    // Package-private: GameSession creates the one developer facade per run.
    DeveloperController(UserController userController) {
        this(userController, new CheatService());
    }

    // Custom CheatService for tests; every dependency is required.
    DeveloperController(UserController userController, CheatService cheats) {
        this.userController = utils.Require.notNull(userController, "userController");
        this.cheats = utils.Require.notNull(cheats, "cheats");
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

    public long getPlayTimeMinutes() {
        return userController.getPlayTimeMinutes();
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
        cheats.setBalance(userController.devControls(), amount);
        userController.notifyObservers(ChangeType.BALANCE);
    }

    // Sets health. Demo only.
    public void setHealth(int hp) {
        cheats.setHealth(userController.devControls(), hp);
        userController.notifyObservers(ChangeType.HEALTH);
    }

    // Sets energy. Demo only.
    public void setEnergy(int energy) {
        cheats.setEnergy(userController.devControls(), energy);
        userController.notifyObservers(ChangeType.ENERGY);
    }

    // Adds playtime in minutes; non-positive values are ignored.
    public void addPlaytimeMinutes(long minutes) {
        if (minutes == 0) {
            return;
        }
        cheats.addPlaytimeMinutes(userController.devControls(), minutes);
        userController.publishAll();
    }

    // Sets playtime to an exact number of minutes. Demo only.
    public void setPlaytimeMinutes(long minutes) {
        cheats.setPlaytimeMinutes(userController.devControls(), minutes);
        userController.publishAll();
    }

    // Sets level. Demo only.
    public void setLevel(int level) {
        cheats.setLevel(userController.devControls(), level);
        userController.notifyObservers(ChangeType.XP);
    }

    // Sets reputation. Demo only.
    public void setReputation(int reputation) {
        cheats.setReputation(userController.devControls(), reputation);
        userController.notifyObservers(ChangeType.REPUTATION);
    }

    // Sets job bonus. Demo only.
    public void setJobBonus(int jobBonus) {
        cheats.setJobBonus(userController.devControls(), jobBonus);
        userController.notifyObservers();
    }

    // Lifts any active block. Demo only.
    public void unblock() {
        userController.unblockUser();
        userController.publishAll();
    }
}

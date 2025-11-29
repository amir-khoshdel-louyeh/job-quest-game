package controller;

import model.player.DevControls;

// Demo/cheat harness for the Developer panel and tests.
// Mutates through the narrow DevControls port, so the domain aggregate keeps
// its Wallet/Vitals/Progress collaborators private.
final class CheatService {

    // ----- Commands -----
    // Sets balance to an exact value. Demo only.
    public void setBalance(DevControls target, int amount) {
        requireTarget(target).setBalance(amount);
    }

    // Sets health to an exact value. Demo only.
    public void setHealth(DevControls target, int hp) {
        requireTarget(target).setHealth(hp);
    }

    // Sets energy to an exact value. Demo only.
    public void setEnergy(DevControls target, int energy) {
        requireTarget(target).setEnergy(energy);
    }

    // Sets level directly. Demo only.
    public void setLevel(DevControls target, int level) {
        requireTarget(target).setLevel(level);
    }

    // Sets reputation directly. Demo only.
    public void setReputation(DevControls target, int reputation) {
        requireTarget(target).setReputation(reputation);
    }

    // Sets job bonus directly. Demo only.
    public void setJobBonus(DevControls target, int jobBonus) {
        requireTarget(target).setJobBonus(jobBonus);
    }

    // Adds playtime in whole minutes; non-positive values are ignored.
    public void addPlaytimeMinutes(DevControls target, long minutes) {
        requireTarget(target).addPlayTimeMinutes(minutes);
    }

    // Sets playtime to an exact number of minutes. Demo only.
    public void setPlaytimeMinutes(DevControls target, long minutes) {
        requireTarget(target).setPlayTimeMinutes(minutes);
    }

    // ----- Private helpers -----
    private DevControls requireTarget(DevControls target) {
        if (target == null) {
            throw new IllegalArgumentException("dev target must not be null");
        }
        return target;
    }
}

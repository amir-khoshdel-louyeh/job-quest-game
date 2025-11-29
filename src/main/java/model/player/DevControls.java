package model.player;

// Demo-only exact-value mutations used by the developer panel.
// User keeps its implementation private and exposes only this port, so
// Wallet/Vitals/CareerProgress/JobRecord/PlayClock stay private fields.
// Game rules never depend on this interface.
public interface DevControls {

    void setBalance(int amount);

    void setHealth(int health);

    void setEnergy(int energy);

    void setLevel(int level);

    void setReputation(int reputation);

    void setJobBonus(int jobBonus);

    // Adds whole minutes; non-positive values are ignored.
    void addPlayTimeMinutes(long minutes);

    // Sets play time to an exact value; negative values become 0.
    void setPlayTimeMinutes(long minutes);
}

package model.player;

// Where a rule puts money, XP, and reputation. Implemented by User, and by
// UserController so reward changes notify observers in one place.
public interface RewardTarget {
    boolean deposit(int amount);

    boolean addExperience(int amount);

    void gainReputation(int amount);
}

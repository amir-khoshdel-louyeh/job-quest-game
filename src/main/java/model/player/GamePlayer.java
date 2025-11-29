package model.player;

import model.Identity;

// What GameController needs from the player: identity/balance for queries and
// the goal surfaces it forwards to the challenge/achievement services.
public interface GamePlayer extends ChallengePlayer, AchievementPlayer {
    Identity getIdentity();

    int getBalance();
}

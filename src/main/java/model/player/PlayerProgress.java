package model.player;

// Composite player surface for code that coordinates both goal services.
// The individual services depend on ChallengePlayer/AchievementPlayer instead.
// Implemented by User (data) and UserController (data + notifications).
public interface PlayerProgress extends ChallengePlayer, AchievementPlayer {
}

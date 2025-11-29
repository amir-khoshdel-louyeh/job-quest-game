package model.player;

import model.goals.AchievementStats;

// Complete read-only player surface.
// Consumers should depend on the narrow slice they actually use
// (JobBoardView, SkillBoardView, PlayerIdentityView, WalletView, ...).
public interface PlayerView extends JobBoardView, SkillBoardView, WalletView,
        VitalsView, ProgressView, BlockView, AchievementStats {

    int getAchievementCount();
}

package services.contract;

import model.career.Job;
import model.career.JobRewards;
import model.minigame.MiniGameResult;
import model.player.PaymentStats;

// The reward math a job attempt needs.
// JobController depends on this contract, not on JobRewardService.
public interface JobRewardRules {

    JobRewards calculateRewards(Job job, PaymentStats stats, MiniGameResult game);

    int reputationPenalty(MiniGameResult game);
}

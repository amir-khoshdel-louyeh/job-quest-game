package controller;

import model.career.Job;
import model.career.JobRewards;
import model.minigame.MiniGameResult;

// What GameController needs from the job module.
// GameController depends on this contract, not on JobController.
public interface JobOperations {

    Outcome<JobRewards> doJob(Job job);

    Outcome<JobRewards> doJob(Job job, MiniGameResult game);
}

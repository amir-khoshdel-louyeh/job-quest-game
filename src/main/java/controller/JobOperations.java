package controller;

import model.career.Job;
import model.minigame.MiniGameResult;

// What GameController needs from the job module.
// GameController depends on this contract, not on JobController.
public interface JobOperations {

    Outcome doJob(Job job);

    Outcome doJob(Job job, MiniGameResult game);
}

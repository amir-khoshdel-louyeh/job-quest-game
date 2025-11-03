package controller;

/**
 * Controller handling job-related game actions such as performing jobs and awarding rewards.
 *
 * Follows Single Responsibility Principle by managing job workflows and coordination only.
 */

import model.Job;

public class JobController {
    private final UserController userController;

    public JobController(UserController userController) {
        // construct job controller with a user controller
        this.userController = userController;
    }

    public boolean doJob(Job job) {
        // attempt to perform the given job for the user
        // Simple pre-check: ensure user has enough energy to start the job.
        if (userController.getEnergy() < job.getEnergyCost()) {
            return false;
        }

        // Real job logic (apply energy cost, award XP, calculate bonuses) would
        // be implemented here. For now this method is a small stub that returns success
        // if preconditions are met. Keep it short and clearly documented for beginners.
        return true;
    }
}

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
        if (userController.getEnergy() < job.getEnergyCost()) return false;
        // Job logic, bonuses, XP, etc. would go here
        // For now, just a stub
        return true;
    }
}

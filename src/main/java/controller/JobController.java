package controller;

import model.Job;

public class JobController {
    private final UserController userController;

    public JobController(UserController userController) {
        this.userController = userController;
    }

    public boolean doJob(Job job) {
        if (userController.getEnergy() < job.getEnergyCost()) return false;
        // Job logic, bonuses, XP, etc. would go here
        // For now, just a stub
        return true;
    }
}

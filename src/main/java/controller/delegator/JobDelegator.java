package controller.delegator;

import controller.GameController;
import controller.JobController;
import controller.UserController;
import model.Job;
import provider.JobProvider;

import java.util.List;

/**
 * Delegates job-related workflows to the job handler.
 */
public class JobDelegator implements Delegator<JobController> {
    private final JobController handler;
    private final UserController userController;

    public JobDelegator(JobController handler, UserController userController) {
        this.handler = handler;
        this.userController = userController;
    }

    public GameController.ActionResult requestJobDialog() {
        try {
            List<Job> availableJobs = JobProvider.getAvailableJobsForUser(userController.getUser());
            if (availableJobs != null && !availableJobs.isEmpty()) {
                return new GameController.ActionResult(true, "JOB_DIALOG_REQUIRED");
            }
            return new GameController.ActionResult(false,
                "No jobs are available for your current identity/skills.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new GameController.ActionResult(false, "Error checking available jobs.");
        }
    }

    public GameController.ActionResult performJob(Job job) {
        boolean success = handler.doJob(job);
        return success
            ? new GameController.ActionResult(true, "Job completed successfully.")
            : new GameController.ActionResult(false, "Failed to complete job.");
    }

    public JobController getHandler() {
        return handler;
    }
}

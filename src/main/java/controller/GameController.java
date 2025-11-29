package controller;

import model.career.Job;
import model.career.JobRewards;
import model.career.LearnableSkill;
import model.minigame.MiniGameResult;
import model.store.Service;

// Runs high-level player actions: buy services, do jobs, learn skills.
// Screen reads (catalogs, progress, session stats) live in GameQueries.
public final class GameController {
    private final EconomyController economyController;
    private final JobController jobController;
    private final SkillController skillController;
    private final SessionTracker session;

    // ----- Construction -----
    // Every collaborator comes from GameSession; all are required, so job,
    // skill, challenge, and achievement rules share the same service objects.
    // Package-private: views use this object, but never build it.
    GameController(EconomyController economyController,
                   JobController jobController,
                   SkillController skillController,
                   SessionTracker session) {
        this.economyController = utils.Require.notNull(economyController, "economyController");
        this.jobController = utils.Require.notNull(jobController, "jobController");
        this.skillController = utils.Require.notNull(skillController, "skillController");
        this.session = utils.Require.notNull(session, "session");
    }

    // ----- Commands -----
    // Buys a service with an instant effect.
    public Outcome<Service> purchaseService(String serviceName) {
        return economyController.purchaseService(serviceName);
    }

    public Outcome<JobRewards> doJob(Job job) {
        return recordJob(jobController.doJob(job));
    }

    public Outcome<JobRewards> doJob(Job job, MiniGameResult game) {
        return recordJob(jobController.doJob(job, game));
    }

    public Outcome<LearnableSkill> learnSkill(String skillName) {
        return skillController.learnSkill(skillName);
    }

    // ----- Private helpers -----
    private Outcome<JobRewards> recordJob(Outcome<JobRewards> result) {
        if (result.isSuccess()) {
            session.recordCompletedJob();
        }
        return result;
    }
}

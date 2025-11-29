package controller;

import model.career.Job;
import model.career.JobRewards;
import model.career.LearnableSkill;
import model.minigame.MiniGameResult;
import model.store.Service;

// Runs high-level player actions: buy services, do jobs, learn skills.
// Screen reads (catalogs, progress, session stats) live in GameQueries.
public final class GameController implements GameCommands {
    private final ServicePurchases purchases;
    private final JobOperations jobs;
    private final SkillOperations skills;
    private final SessionStats session;

    // ----- Construction -----
    // Every collaborator is an abstraction supplied by GameSession, so this
    // facade never depends on a concrete controller.
    // Package-private: views use this object, but never build it.
    GameController(ServicePurchases purchases,
                   JobOperations jobs,
                   SkillOperations skills,
                   SessionStats session) {
        this.purchases = utils.Require.notNull(purchases, "purchases");
        this.jobs = utils.Require.notNull(jobs, "jobs");
        this.skills = utils.Require.notNull(skills, "skills");
        this.session = utils.Require.notNull(session, "session");
    }

    // ----- Commands -----
    // Buys a service with an instant effect.
    @Override
    public Outcome<Service> purchaseService(String serviceName) {
        return purchases.purchaseService(serviceName);
    }

    @Override
    public Outcome<JobRewards> doJob(Job job) {
        return recordJob(jobs.doJob(job));
    }

    @Override
    public Outcome<JobRewards> doJob(Job job, MiniGameResult game) {
        return recordJob(jobs.doJob(job, game));
    }

    @Override
    public Outcome<LearnableSkill> learnSkill(String skillName) {
        return skills.learnSkill(skillName);
    }

    // ----- Private helpers -----
    private Outcome<JobRewards> recordJob(Outcome<JobRewards> result) {
        if (result.isSuccess()) {
            session.recordCompletedJob();
        }
        return result;
    }
}

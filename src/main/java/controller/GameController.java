package controller;

import controller.contract.GameCommands;
import controller.contract.JobOperations;
import controller.contract.ShopPurchases;
import controller.contract.SessionStats;
import controller.contract.SkillOperations;
import model.career.Job;
import model.minigame.MiniGameResult;
import utils.Require;

// Runs high-level player actions: buy services, do jobs, learn skills.
// Screen reads (catalogs, progress, session stats) live in GameQueries.
public final class GameController implements GameCommands {
    private final ShopPurchases purchases;
    private final JobOperations jobs;
    private final SkillOperations skills;
    private final SessionStats session;

    // ----- Construction -----
    // Every collaborator is an abstraction supplied by GameSession, so this
    // facade never depends on a concrete controller.
    // Package-private: views use this object, but never build it.
    GameController(ShopPurchases purchases,
                   JobOperations jobs,
                   SkillOperations skills,
                   SessionStats session) {
        this.purchases = Require.notNull(purchases, "purchases");
        this.jobs = Require.notNull(jobs, "jobs");
        this.skills = Require.notNull(skills, "skills");
        this.session = Require.notNull(session, "session");
    }

    // ----- Commands -----
    // Buys a service with an instant effect.
    @Override
    public Outcome purchaseService(String serviceName) {
        return purchases.purchaseService(serviceName);
    }

    @Override
    public Outcome doJob(Job job) {
        return recordJob(jobs.doJob(job));
    }

    @Override
    public Outcome doJob(Job job, MiniGameResult game) {
        return recordJob(jobs.doJob(job, game));
    }

    @Override
    public Outcome learnSkill(String skillName) {
        return skills.learnSkill(skillName);
    }

    // ----- Private helpers -----
    private Outcome recordJob(Outcome result) {
        if (result.isSuccess()) {
            session.recordCompletedJob();
        }
        return result;
    }
}

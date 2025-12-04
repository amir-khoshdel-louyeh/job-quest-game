package services.job;

import model.career.Profession;
import model.career.Job;
import model.career.SkillSet;
import services.contract.JobEligibility;

// Decides whether one job attempt may start: existence, block, profession,
// skills, and energy. Kept separate from reward/XP orchestration.
public final class JobGate implements JobEligibility {

    // ----- Queries -----
    @Override
    public JobGateDecision check(Job job, Profession profession, SkillSet skills,
                                 int energy, boolean blocked) {
        if (job == null) {
            return JobGateDecision.reject("No job selected.");
        }
        if (blocked) {
            return JobGateDecision.reject("You are blocked. Rest and try later.");
        }
        if (!job.isAvailableFor(profession, skills)) {
            return JobGateDecision.reject(
                    "Not available for your profession/skills. Learn more skills!");
        }
        if (energy < job.getEnergyCost()) {
            return JobGateDecision.reject("Not enough energy (need " + job.getEnergyCost()
                    + ", have " + energy + "). Eat or rest first.");
        }
        return JobGateDecision.allow();
    }
}

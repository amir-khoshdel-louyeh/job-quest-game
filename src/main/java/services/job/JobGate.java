package services.job;

import model.career.Profession;
import model.career.Job;
import model.career.SkillSet;
import services.GateDecision;
import services.contract.JobEligibility;

// Decides whether one job attempt may start: existence, block, profession,
// skills, and energy. Kept separate from reward/XP orchestration.
public final class JobGate implements JobEligibility {

    // ----- Queries -----
    @Override
    public GateDecision check(Job job, Profession profession, SkillSet skills,
                              int energy, boolean blocked) {
        if (job == null) {
            return GateDecision.reject("No job selected.");
        }
        if (blocked) {
            return GateDecision.reject("You are blocked. Rest and try later.");
        }
        if (!job.isAvailableFor(profession, skills)) {
            return GateDecision.reject(
                    "Not available for your profession/skills. Learn more skills!");
        }
        if (energy < job.getEnergyCost()) {
            return GateDecision.reject("Not enough energy (need " + job.getEnergyCost()
                    + ", have " + energy + "). Eat or rest first.");
        }
        return GateDecision.allow();
    }
}

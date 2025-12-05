package services.contract;

import model.career.Profession;
import model.career.Job;
import model.career.SkillSet;
import services.GateDecision;

// Decides whether one job attempt may start.
// JobController depends on this contract, not on JobGate.
public interface JobEligibility {

    GateDecision check(Job job, Profession profession, SkillSet skills,
                       int energy, boolean blocked);
}

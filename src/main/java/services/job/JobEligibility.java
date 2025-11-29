package services.job;

import model.Identity;
import model.career.Job;
import model.career.SkillSet;

// Decides whether one job attempt may start.
// JobController depends on this contract, not on JobGate.
public interface JobEligibility {

    JobGateDecision check(Job job, Identity identity, SkillSet skills,
                          int energy, boolean blocked);
}

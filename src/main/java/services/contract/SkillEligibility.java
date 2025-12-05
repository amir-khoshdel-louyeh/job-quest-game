package services.contract;

import model.career.Skill;
import model.career.Profession;
import model.career.SkillSet;
import services.GateDecision;

// Decides whether one skill may be learned right now.
// SkillController depends on this contract, not on SkillGate.
public interface SkillEligibility {

    GateDecision check(Skill skill, Profession profession, SkillSet skills,
                       long playTimeMinutes, int balance);
}

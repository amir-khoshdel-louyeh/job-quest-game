package services.skill;

import model.career.Skill;
import model.career.Profession;
import model.career.SkillAvailability;
import model.career.SkillSet;
import services.GateDecision;
import services.contract.SkillEligibility;

// Decides whether one skill attempt may start: existence, profession,
// availability, and money. Kept separate from purchase/goal orchestration.
public final class SkillGate implements SkillEligibility {

    // ----- Queries -----
    @Override
    public GateDecision check(Skill skill, Profession profession, SkillSet skills,
                              long playTimeMinutes, int balance) {
        if (skill == null) {
            return GateDecision.reject("That skill does not exist.");
        }
        if (!skill.isForProfession(profession)) {
            return GateDecision.reject("That skill is not available for your profession.");
        }

        SkillAvailability availability = skill.availabilityFor(skills, playTimeMinutes);
        if (availability == SkillAvailability.LEARNED) {
            return GateDecision.reject("You already learned that skill.");
        }
        if (availability == SkillAvailability.LOCKED) {
            return GateDecision.reject("You need more playtime before that skill unlocks.");
        }

        if (balance < skill.getCost()) {
            return GateDecision.reject("You do not have enough money for that skill.");
        }
        return GateDecision.allow();
    }
}

package services.job;

import model.Identity;
import model.career.Job;
import model.career.SkillSet;

// Decides whether one job attempt may start: existence, block, profession,
// skills, and energy. Kept separate from reward/XP orchestration.
public final class JobGate {

    // ----- Result -----
    public static final class Decision {
        private final boolean allowed;
        private final String reason;

        private Decision(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }

        public static Decision allow() {
            return new Decision(true, "");
        }

        public static Decision reject(String reason) {
            return new Decision(false, reason == null ? "" : reason);
        }

        public boolean isAllowed() {
            return allowed;
        }

        // Empty when allowed.
        public String getReason() {
            return reason;
        }
    }

    // ----- Queries -----
    public Decision check(Job job, Identity identity, SkillSet skills,
                          int energy, boolean blocked) {
        if (job == null) {
            return Decision.reject("No job selected.");
        }
        if (blocked) {
            return Decision.reject("You are blocked. Rest and try later.");
        }
        if (!job.isAvailableFor(identity, skills)) {
            return Decision.reject(
                    "Not available for your identity/skills. Learn more skills!");
        }
        if (energy < job.getEnergyCost()) {
            return Decision.reject("Not enough energy (need " + job.getEnergyCost()
                    + ", have " + energy + "). Eat or rest first.");
        }
        return Decision.allow();
    }
}

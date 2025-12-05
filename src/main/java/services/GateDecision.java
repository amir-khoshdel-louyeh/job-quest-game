package services;

// Outcome of one eligibility check: allowed, or rejected with a reason shown
// to the player. Shared by every gate (jobs, skills) so the value contract
// lives in one place instead of being copied per module.
public final class GateDecision {
    private final boolean allowed;
    private final String reason;

    private GateDecision(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = reason;
    }

    public static GateDecision allow() {
        return new GateDecision(true, "");
    }

    public static GateDecision reject(String reason) {
        return new GateDecision(false, reason == null ? "" : reason);
    }

    public boolean isAllowed() {
        return allowed;
    }

    // Empty when allowed.
    public String getReason() {
        return reason;
    }
}

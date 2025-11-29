package services.job;

// Outcome of one JobGate check: allowed, or rejected with a reason shown to
// the player. A value object, kept as its own type rather than nested in the
// gate so the check and its result can be read independently.
public final class JobGateDecision {
    private final boolean allowed;
    private final String reason;

    private JobGateDecision(boolean allowed, String reason) {
        this.allowed = allowed;
        this.reason = reason;
    }

    public static JobGateDecision allow() {
        return new JobGateDecision(true, "");
    }

    public static JobGateDecision reject(String reason) {
        return new JobGateDecision(false, reason == null ? "" : reason);
    }

    public boolean isAllowed() {
        return allowed;
    }

    // Empty when allowed.
    public String getReason() {
        return reason;
    }
}

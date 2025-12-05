package controller;

import java.util.Objects;

// One result shape for every controller action: did it work, and what should the
// view say.
public final class Outcome {
    private final boolean success;
    private final String message;

    // ----- Construction -----
    private Outcome(boolean success, String message) {
        this.success = success;
        this.message = Objects.requireNonNullElse(message, "");
    }

    // ----- Factories -----
    public static Outcome success(String message) {
        return new Outcome(true, message);
    }

    public static Outcome failure(String message) {
        return new Outcome(false, message);
    }

    // ----- Getter -----
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

package controller;

import java.util.Optional;

// One result shape for every controller action: did it work, what should the
// view say, and what (if anything) came back.
public final class Outcome<T> {
    private final boolean success;
    private final String message;
    private final T payload;

    // ----- Construction -----
    private Outcome(boolean success, String message, T payload) {
        this.success = success;
        this.message = message == null ? "" : message;
        this.payload = payload;
    }

    // ----- Factories -----
    public static <T> Outcome<T> success(String message, T payload) {
        return new Outcome<T>(true, message, payload);
    }

    public static <T> Outcome<T> success(String message) {
        return new Outcome<T>(true, message, null);
    }

    public static <T> Outcome<T> failure(String message) {
        return new Outcome<T>(false, message, null);
    }

    // ----- Getter -----
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Empty on failure or on a payload-less success.
    public Optional<T> getPayload() {
        return Optional.ofNullable(payload);
    }
}

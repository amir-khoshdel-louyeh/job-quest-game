package utils;

// Error reporting; failures stay visible and greppable.
public final class GameLog {
    private GameLog() {
    }

    public static void error(String context, Throwable problem) {
        String where = context == null ? "game" : context;
        String detail = problem == null ? "unknown error" : String.valueOf(problem);
        System.err.println("[GameLog] " + where + ": " + detail);
        if (problem != null) {
            problem.printStackTrace(System.err);
        }
    }
}

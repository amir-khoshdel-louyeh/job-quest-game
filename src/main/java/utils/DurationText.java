package utils;

// Time conversions and labels used by stats, school, and dev screens.
// Single place for the "1000 * 60" math and the "2h 30m" format.
public final class DurationText {
    public static final long MILLIS_PER_MINUTE = 1000L * 60;

    private DurationText() {
    }

    // Whole minutes in a millisecond span.
    public static long minutesOf(long millis) {
        if (millis <= 0) {
            return 0;
        }
        return millis / MILLIS_PER_MINUTE;
    }

    // Formats minutes as "2h", "1h 30m", or "45 min".
    public static String formatMinutes(long minutes) {
        if (minutes <= 0) {
            return "0 min";
        }
        if (minutes >= 60 && minutes % 60 == 0) {
            return (minutes / 60) + "h";
        }
        if (minutes >= 60) {
            return (minutes / 60) + "h " + (minutes % 60) + "m";
        }
        return minutes + " min";
    }
}

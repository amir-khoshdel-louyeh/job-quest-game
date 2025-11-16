package utils;

// Single home for 0-100 progress math. Replaces the duplicated
// percentage code in CareerProgress, Challenge and Achievement.
public final class ProgressUtil {
    private ProgressUtil() {
    }

    public static int percent(int current, int target) {
        if (target <= 0) {
            return 100;
        }
        if (current <= 0) {
            return 0;
        }
        double ratio = current / (double) target;
        int result = (int) (ratio * 100);
        if (result < 0) {
            return 0;
        }
        if (result > 100) {
            return 100;
        }
        return result;
    }
}

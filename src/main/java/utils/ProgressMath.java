package utils;

// 0-100 progress math.
// Shared by CareerProgress, Challenge, and Achievement.
public final class ProgressMath {
    private ProgressMath() {
    }

    public static int percent(double current, double target) {
        if (target <= 0) {
            return 100;
        }
        if (current <= 0) {
            return 0;
        }
        double ratio = current / target;
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

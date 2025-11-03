package utils;

/**
 * Utility methods for randomization used by providers, events and gameplay logic.
 *
 * Follows Single Responsibility Principle by grouping random helper functions only.
 */

import java.util.Random;

public class RandomUtil {
    private static Random random = new Random();

    public static int nextInt(int bound) {
        // return a random integer between 0 (inclusive) and bound (exclusive)
        return random.nextInt(bound);
    }
}

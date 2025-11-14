package utils;

import java.util.Random;

// Picks random numbers for the game.
public class RandomUtil {
    private static Random random = new Random();

    public static int getRandomInt(int bound) {
        // return a random integer between 0 (inclusive) and bound (exclusive)
        return random.nextInt(bound);
    }
}

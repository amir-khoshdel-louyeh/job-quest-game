package utils;

// Gameplay tuning numbers.
// Edit here to rebalance the game.
public final class GameBalance {
    private GameBalance() {
    }

    // Sickness check.
    public static final long SICKNESS_COOLDOWN_MILLIS = 60 * 60 * 1000L;
    public static final int SICKNESS_ROLL_BOUND = 100;
    public static final int SICKNESS_ROLL_HIT_BELOW = 10;
    public static final int SICKNESS_MIN_HEALTH_LOSS = 5;
    public static final int SICKNESS_HEALTH_LOSS_RANGE = 6;

    // Passive drains.
    public static final int BASE_ENERGY_LOSS = 1;
    public static final int LOW_HEALTH_THRESHOLD = 30;
    public static final int SICKLY_ENERGY_LOSS = 2;
    public static final int BASE_HEALTH_LOSS = 1;
    public static final int LOW_ENERGY_THRESHOLD = 25_000;
    public static final int EXHAUSTED_HEALTH_LOSS = 3;

    // Knock-out penalty.
    public static final int KNOCKOUT_MONEY_PENALTY = 200;
    public static final long KNOCKOUT_BLOCK_MILLIS = 5 * 60 * 1000L;
}

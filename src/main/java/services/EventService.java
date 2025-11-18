package services;

import java.util.Random;
import java.util.function.Supplier;

import model.player.User;
import model.store.GameEvent;
import provider.GameEventProvider;

// Hands out surprise events like finding money or losing health.
// Instance (not static): the cooldown belongs to this object, and the
// clock/random come from outside — so behavior is deterministic in tests
// and independent per game session (no static global state).
public class EventService {
    private static final long EVENT_COOLDOWN = 5 * 60 * 1000; // 5 minutes between events
    private static final double EVENT_CHANCE = 0.20; // 20% chance each check

    private final Random random;
    private final Supplier<Long> clock;
    private long lastEventTime = 0;

    public EventService() {
        this(new Random(), System::currentTimeMillis);
    }

    // Test seam: fixed clock + seeded random make every run reproducible.
    public EventService(Random random, Supplier<Long> clock) {
        if (random == null) {
            throw new IllegalArgumentException("random must not be null");
        }
        if (clock == null) {
            throw new IllegalArgumentException("clock must not be null");
        }
        this.random = random;
        this.clock = clock;
    }

    // Checks for a random event. At most one every 5 minutes, 20% chance each check.
    public GameEvent checkForRandomEvent(User user) {
        long currentTime = clock.get();

        // Don't trigger events too frequently
        if (currentTime - lastEventTime < EVENT_COOLDOWN) {
            return null;
        }

        // 20% chance for an event to occur when checked
        if (random.nextDouble() > EVENT_CHANCE) {
            return null;
        }

        // Picks a random event.
        GameEvent event = GameEventProvider.getRandomEvent(random.nextDouble());
        if (event != null) {
            applyEventEffects(user, event);
            lastEventTime = currentTime;
        }

        return event;
    }

    // Applies the event's money, health, and energy effects to the player.
    private void applyEventEffects(User user, GameEvent event) {
        // Apply money effect
        if (event.getMoneyEffect() > 0) {
            user.deposit(event.getMoneyEffect());
        } else if (event.getMoneyEffect() < 0) {
            user.withdraw(-event.getMoneyEffect());
        }

        // Apply health effect
        if (event.getHealthEffect() > 0) {
            user.gainHealth(event.getHealthEffect());
        } else if (event.getHealthEffect() < 0) {
            user.loseHealth(-event.getHealthEffect());
        }

        // Apply energy effect
        if (event.getEnergyEffect() > 0) {
            user.gainEnergy(event.getEnergyEffect());
        } else if (event.getEnergyEffect() < 0) {
            user.loseEnergy(-event.getEnergyEffect());
        }

        // Positive events can give small reputation boost
        if (event.getType() == GameEvent.EventType.POSITIVE) {
            user.addReputation(1);
        }
    }
}

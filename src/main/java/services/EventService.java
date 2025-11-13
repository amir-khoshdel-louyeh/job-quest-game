package services;

import model.GameEvent;
import model.User;
import provider.GameEventProvider;

// This class gives random events to the player (like finding money).
public class EventService {
    private static EventService instance;
    private long lastEventTime = 0;
    private static final long EVENT_COOLDOWN = 5 * 60 * 1000; // 5 minutes between events
    
    private EventService() {}
    // private constructor for singleton
    
    public static EventService getInstance() {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }
    
    // Check if a random event happens now.
    // At most one event every 5 minutes, 20% chance each check.
    // Uses simple if, no stream.
    public GameEvent checkForRandomEvent(User user) {
        long currentTime = System.currentTimeMillis();
        
        // Don't trigger events too frequently
        if (currentTime - lastEventTime < EVENT_COOLDOWN) {
            return null;
        }
        
        // 20% chance for an event to occur when checked
        if (Math.random() > 0.20) {
            return null;
        }

        // get random event using simple provider method
        GameEvent event = GameEventProvider.getRandomEvent();
        if (event != null) {
            applyEventEffects(user, event);
            lastEventTime = currentTime;
        }
        
        return event;
    }
    
    // apply event numeric effects (money/health/energy) to the user
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

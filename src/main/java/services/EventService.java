package services;

import model.GameEvent;
import model.User;
import provider.GameEventProvider;
/**
 * Manages random events and their effects on the player.
 */
public class EventService {
    private static EventService instance;
    private long lastEventTime = 0;
    private static final long EVENT_COOLDOWN = 5 * 60 * 1000; // 5 minutes between events
    
    private EventService() {}
    
    public static EventService getInstance() {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }
    
    /** Game event for the user */
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
        
    GameEvent event = provider.GameEventProvider.getRandomEvent();
        if (event != null) {
            applyEventEffects(user, event);
            lastEventTime = currentTime;
        }
        
        return event;
    }
    
    /** Apply the effects of an event to the user */
    private void applyEventEffects(User user, GameEvent event) {
        // Apply money effect
        if (event.getMoneyEffect() != 0) {
            int newBalance = user.getBalance() + event.getMoneyEffect();
            user.setBalance(Math.max(0, newBalance));
        }
        
        // Apply health effect
        if (event.getHealthEffect() != 0) {
            int newHealth = user.getHealth() + event.getHealthEffect();
            user.setHealth(newHealth);
        }
        
        // Apply energy effect
        if (event.getEnergyEffect() != 0) {
            int newEnergy = user.getEnergy() + event.getEnergyEffect();
            user.setEnergy(newEnergy);
        }
        
        // Positive events can give small reputation boost
        if (event.getType() == GameEvent.EventType.POSITIVE) {
            user.addReputation(1);
        }
    }
    
    /** Trigger a specific event by ID */
    public void triggerEvent(User user, String eventId) {
        for (GameEvent event : GameEventProvider.getAllEvents()) {
            if (event.getId().equals(eventId)) {
                applyEventEffects(user, event);
                break;
            }
        }
    }
}

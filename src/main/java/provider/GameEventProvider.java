package provider;

import java.util.ArrayList;
import java.util.List;

import model.GameEvent;

/**
 * Provides all random events that can occur in the game.
 */
public class GameEventProvider {
    
    private static final List<GameEvent> ALL_EVENTS = new ArrayList<>();
    
    static {
        // POSITIVE EVENTS
        ALL_EVENTS.add(new GameEvent(
            "found_money",
            "Lucky Find!",
            "You found some money on the street!",
            GameEvent.EventType.POSITIVE,
            500, 0, 0, 0.05
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "bonus_client",
            "Bonus Payment",
            "A happy client gave you a bonus!",
            GameEvent.EventType.POSITIVE,
            1000, 0, 0, 0.08
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "energy_boost",
            "Good Rest",
            "You had an excellent night's sleep!",
            GameEvent.EventType.POSITIVE,
            0, 0, 5000, 0.10
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "health_boost",
            "Vitamin Boost",
            "A friend gave you some healthy food!",
            GameEvent.EventType.POSITIVE,
            0, 10, 0, 0.08
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "perfect_day",
            "Perfect Day!",
            "Everything is going great today!",
            GameEvent.EventType.POSITIVE,
            500, 5, 2000, 0.03
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "tax_refund",
            "Tax Refund",
            "You received an unexpected tax refund!",
            GameEvent.EventType.POSITIVE,
            2000, 0, 0, 0.02
        ));
        
        // NEGATIVE EVENTS
        ALL_EVENTS.add(new GameEvent(
            "pc_problem",
            "Computer Issues",
            "Your computer is running slow today.",
            GameEvent.EventType.NEGATIVE,
            0, 0, -2000, 0.10
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "headache",
            "Headache",
            "You have a headache and need to rest.",
            GameEvent.EventType.NEGATIVE,
            0, -10, -1000, 0.08
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "unexpected_bill",
            "Unexpected Bill",
            "You received an unexpected bill to pay.",
            GameEvent.EventType.NEGATIVE,
            -800, 0, 0, 0.07
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "internet_down",
            "Internet Problems",
            "Your internet connection is unstable today.",
            GameEvent.EventType.NEGATIVE,
            0, 0, -3000, 0.05
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "bad_weather",
            "Bad Weather",
            "The gloomy weather is affecting your mood.",
            GameEvent.EventType.NEGATIVE,
            0, -5, -1000, 0.12
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "equipment_repair",
            "Equipment Repair",
            "You had to repair your work equipment.",
            GameEvent.EventType.NEGATIVE,
            -1500, 0, 0, 0.04
        ));
        
        // NEUTRAL/INFORMATIVE EVENTS
        ALL_EVENTS.add(new GameEvent(
            "motivation",
            "Motivation",
            "You feel motivated to work harder!",
            GameEvent.EventType.NEUTRAL,
            0, 0, 1000, 0.15
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "coffee_break",
            "Coffee Break",
            "You took a nice coffee break.",
            GameEvent.EventType.NEUTRAL,
            -50, 3, 500, 0.10
        ));
        
        ALL_EVENTS.add(new GameEvent(
            "new_opportunity",
            "New Opportunity",
            "You learned about a new freelance platform!",
            GameEvent.EventType.NEUTRAL,
            0, 0, 0, 0.05
        ));
    }
    
    public static List<GameEvent> getAllEvents() {
        return new ArrayList<>(ALL_EVENTS);
    }
    
    public static GameEvent getRandomEvent() {
        // pick a random event based on defined probabilities
        double rand = Math.random();
        double cumulative = 0.0;
        
        for (GameEvent event : ALL_EVENTS) {
            cumulative += event.getProbability();
            if (rand <= cumulative) {
                return event;
            }
        }
        
        return null; // No event triggered
    }
}

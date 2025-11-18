package controller;

import model.store.GameEvent;
import model.player.User;
import services.EventService;

// Triggers random events and applies their effects.
public class EventController {
    private final EventService events;

    public EventController() {
        this(new EventService());
    }

    // Test seam: inject a service with fixed clock/random.
    public EventController(EventService events) {
        if (events == null) {
            throw new IllegalArgumentException("events must not be null");
        }
        this.events = events;
    }

    public GameEvent triggerRandomEvent(User user) {
        return events.checkForRandomEvent(user);
    }
}

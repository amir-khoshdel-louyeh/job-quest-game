package controller;

import model.store.GameEvent;
import model.player.User;
import services.EventService;

// Triggers random events and applies their effects.
public class EventController {
    public EventController() {
    }

    public GameEvent triggerRandomEvent(User user) {
        return EventService.checkForRandomEvent(user);
    }
}

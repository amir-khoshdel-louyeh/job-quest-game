package controller;

/**
 * Controller that manages game events and their effects on users.
 *
 * Follows Single Responsibility Principle by orchestrating event triggering and handling only.
 */

import model.GameEvent;
import model.User;
import services.EventService;

public class EventController {
    private final EventService eventService;

    public EventController() {
        // initialize event service for event handling
        this.eventService = EventService.getInstance();
    }

    public GameEvent triggerRandomEvent(User user) {
        // trigger a possible random event for the given user
        return eventService.checkForRandomEvent(user);
    }
}

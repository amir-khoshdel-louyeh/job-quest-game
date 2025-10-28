package controller;

import model.GameEvent;
import model.User;
import services.EventService;

public class EventController {
    private final EventService eventService;

    public EventController() {
        this.eventService = EventService.getInstance();
    }

    public GameEvent triggerRandomEvent(User user) {
        return eventService.checkForRandomEvent(user);
    }
}

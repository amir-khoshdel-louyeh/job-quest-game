package controller;

// Where routine/dev messages go. RoutineController depends on this
// abstraction instead of the concrete Swing panel (DIP): it stays
// testable without Swing, and the controller->view cycle is broken.
public interface MessageSink {
    void addChatMessage(String message);
}

package controller;

// Chat message target; implemented by Swing panels.
public interface MessageSink {
    void addChatMessage(String message);
}

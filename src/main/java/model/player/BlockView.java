package model.player;

// Whether jobs are blocked and until when.
public interface BlockView {
    boolean isBlocked();

    long getBlockedUntil();
}

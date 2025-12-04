package view.minigame;

// Stopwatch for elapsed-time measurement.
// The quiz dialog owns one to time a round.
public final class ElapsedTimer {
    private long startMillis;

    public ElapsedTimer() {
        start();
    }

    public void start() {
        startMillis = System.currentTimeMillis();
    }

    public int elapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startMillis) / 1000);
    }
}

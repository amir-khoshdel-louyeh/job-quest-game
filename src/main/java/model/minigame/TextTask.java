package model.minigame;

/**
 * A retype task: Typists copy prose, Programmers copy tiny programs.
 *
 * <p>Immutable value object (Encapsulation). {@code parSeconds} is the
 * reference time for full speed points; {@code difficulty} (1–3) only
 * labels the UI badge.</p>
 */
public final class TextTask {
    private final String id;
    private final String title;
    private final String content;
    private final int parSeconds;
    private final int difficulty;

    public TextTask(String id, String title, String content, int parSeconds, int difficulty) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content required");
        this.id = id;
        this.title = title == null ? id : title;
        this.content = content;
        this.parSeconds = Math.max(10, parSeconds);
        this.difficulty = Math.max(1, Math.min(3, difficulty));
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getParSeconds() { return parSeconds; }
    public int getDifficulty() { return difficulty; }

    /** Difficulty badge for the picker list. */
    public String difficultyStars() {
        return "★".repeat(difficulty) + "☆".repeat(3 - difficulty);
    }
}

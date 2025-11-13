package model.minigame;

// One typing task: title + text to copy + time limit.
public final class TextTask {
    private String id;
    private String title;
    private String content;
    private int parSeconds;
    private int difficulty;

    // Constructor with simple if checks (no isBlank, no Math).
    public TextTask(String id, String title, String content, int parSeconds, int difficulty) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("id required");
        }
        if (content == null || content.trim().equals("")) {
            throw new IllegalArgumentException("content required");
        }
        this.id = id;
        if (title == null) {
            this.title = id;
        } else {
            this.title = title;
        }
        this.content = content;
        this.parSeconds = parSeconds;
        if (this.parSeconds < 10) {
            this.parSeconds = 10;
        }
        this.difficulty = difficulty;
        if (this.difficulty < 1) {
            this.difficulty = 1;
        }
        if (this.difficulty > 3) {
            this.difficulty = 3;
        }
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public int getParSeconds() {
        return parSeconds;
    }
    public int getDifficulty() {
        return difficulty;
    }

    // Show stars like "**-" for difficulty, uses for loop (no repeat).
    public String difficultyStars() {
        String result = "";
        for (int i = 0; i < difficulty; i++) {
            result = result + "*";
        }
        for (int i = difficulty; i < 3; i++) {
            result = result + "-";
        }
        return result;
    }
}

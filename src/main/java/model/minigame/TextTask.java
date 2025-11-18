package model.minigame;

import java.util.Objects;

// One typing task with text and a time limit.
public final class TextTask {
    private String id;
    private String title;
    private String content;
    private int parSeconds;
    private int difficulty;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TextTask other = (TextTask) obj;
        return parSeconds == other.parSeconds
                && difficulty == other.difficulty
                && Objects.equals(id, other.id)
                && Objects.equals(title, other.title)
                && Objects.equals(content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, parSeconds, difficulty);
    }

    @Override
    public String toString() {
        return title + " [" + id + "]";
    }
}

package model.minigame;

import java.util.Collections;
import java.util.List;

/**
 * One multiple-choice work question (Doctor diagnosis / Designer brief).
 *
 * <p>Immutable; options list is defensively copied. {@code category} selects
 * which provider list the question belongs to.</p>
 */
public final class QuizQuestion {

    public enum Category { DIAGNOSIS, DESIGN }

    private final String id;
    private final Category category;
    private final String prompt;
    private final List<String> options;
    private final int correctIndex;
    private final String explanation;

    public QuizQuestion(String id, Category category, String prompt,
                        List<String> options, int correctIndex, String explanation) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (options == null || options.size() < 2) throw new IllegalArgumentException("need 2+ options");
        if (correctIndex < 0 || correctIndex >= options.size()) throw new IllegalArgumentException("bad correctIndex");
        this.id = id;
        this.category = category;
        this.prompt = prompt;
        this.options = Collections.unmodifiableList(new java.util.ArrayList<>(options));
        this.correctIndex = correctIndex;
        this.explanation = explanation == null ? "" : explanation;
    }

    public String getId() { return id; }
    public Category getCategory() { return category; }
    public String getPrompt() { return prompt; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }

    public boolean isCorrect(int picked) { return picked == correctIndex; }
}

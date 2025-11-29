package model.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utils.Lists;

// One quiz question with answer options.
// Read via quiz provider/controller.
public final class QuizQuestion {

    private final String id;
    private final String prompt;
    private final ArrayList<String> options;
    private final int correctIndex;
    private final String explanation;

    // ----- Construction -----
    public QuizQuestion(String id, String prompt,
                        List<String> options, int correctIndex, String explanation) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("id required");
        }
        if (options == null || options.size() < 2) {
            throw new IllegalArgumentException("need 2+ options");
        }
        if (correctIndex < 0 || correctIndex >= options.size()) {
            throw new IllegalArgumentException("bad correctIndex");
        }
        this.id = id;
        this.prompt = prompt;
        this.options = new ArrayList<String>(Lists.copyOf(options));
        this.correctIndex = correctIndex;
        if (explanation == null) {
            this.explanation = "";
        } else {
            this.explanation = explanation;
        }
    }

    // ----- Getter -----
    public String getPrompt() {
        return prompt;
    }
    public List<String> getOptions() {
        return Lists.copyOf(options);
    }
    public int getCorrectIndex() {
        return correctIndex;
    }
    public String getExplanation() {
        return explanation;
    }

    // ----- Queries -----
    public boolean isCorrect(int picked) {
        return picked == correctIndex;
    }

    // ----- Object overrides -----
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QuizQuestion other = (QuizQuestion) obj;
        return correctIndex == other.correctIndex
                && Objects.equals(id, other.id)
                && Objects.equals(prompt, other.prompt)
                && Objects.equals(options, other.options)
                && Objects.equals(explanation, other.explanation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prompt, options, correctIndex, explanation);
    }

    @Override
    public String toString() {
        return prompt + " [" + id + "]";
    }
}

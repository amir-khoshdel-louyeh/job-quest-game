package model.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// One quiz question with answer options.
public final class QuizQuestion {

    private String id;
    private String prompt;
    private ArrayList<String> options;
    private int correctIndex;
    private String explanation;

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
        this.options = new ArrayList<String>();
        for (int i = 0; i < options.size(); i++) {
            this.options.add(options.get(i));
        }
        this.correctIndex = correctIndex;
        if (explanation == null) {
            this.explanation = "";
        } else {
            this.explanation = explanation;
        }
    }

    public String getPrompt() {
        return prompt;
    }
    // Returns a copy so the stored list can't be changed from outside.
    public List<String> getOptions() {
        ArrayList<String> copy = new ArrayList<String>();
        for (int i = 0; i < options.size(); i++) {
            copy.add(options.get(i));
        }
        return copy;
    }
    public int getCorrectIndex() {
        return correctIndex;
    }
    public String getExplanation() {
        return explanation;
    }

    public boolean isCorrect(int picked) {
        return picked == correctIndex;
    }

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

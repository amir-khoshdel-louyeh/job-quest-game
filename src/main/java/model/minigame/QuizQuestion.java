package model.minigame;

import java.util.ArrayList;
import java.util.List;

// One quiz question with answer options.
public final class QuizQuestion {

    public enum Category { DIAGNOSIS, DESIGN }

    private String id;
    private Category category;
    private String prompt;
    private ArrayList<String> options;
    private int correctIndex;
    private String explanation;

    public QuizQuestion(String id, Category category, String prompt,
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
        this.category = category;
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

    public String getId() {
        return id;
    }
    public Category getCategory() {
        return category;
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
}

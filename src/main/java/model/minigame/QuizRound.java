package model.minigame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import utils.Lists;

// One quiz round: the chosen questions, the player's picks, and scoring.
// The dialog only renders this object and forwards clicks.
public final class QuizRound {
    public static final int QUESTIONS_PER_ROUND = 3;
    public static final int UNANSWERED = -2;
    public static final int SKIPPED = -1;

    private final String gameName;
    private final ArrayList<QuizQuestion> questions;
    private final ArrayList<Integer> picked;
    private final ScoreStrategy scorer;
    private int index;

    // ----- Construction -----
    public QuizRound(String gameName, List<QuizQuestion> questions, ScoreStrategy scorer) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("a quiz round needs at least one question");
        }
        if (scorer == null) {
            throw new IllegalArgumentException("scorer must not be null");
        }
        this.gameName = gameName;
        this.questions = new ArrayList<QuizQuestion>(Lists.copyOf(questions));
        this.scorer = scorer;
        this.picked = new ArrayList<Integer>();
        for (int i = 0; i < this.questions.size(); i++) {
            this.picked.add(UNANSWERED);
        }
    }

    // Builds a shuffled round with at most `count` questions.
    public static QuizRound random(String gameName, List<QuizQuestion> bank, int count,
                                   Random random, ScoreStrategy scorer) {
        if (bank == null || bank.isEmpty()) {
            throw new IllegalArgumentException("question bank must not be empty");
        }
        ArrayList<QuizQuestion> shuffled = new ArrayList<QuizQuestion>(Lists.copyOf(bank));
        Collections.shuffle(shuffled, random);
        int take = Math.min(count, shuffled.size());
        return new QuizRound(gameName, shuffled.subList(0, take), scorer);
    }

    // ----- Getter -----
    public int size() {
        return questions.size();
    }

    public int getIndex() {
        return index;
    }

    public QuizQuestion getCurrentQuestion() {
        return questions.get(index);
    }

    public List<Integer> getAnswers() {
        return Lists.copyOf(picked);
    }

    // ----- Queries -----
    public boolean isAnswered(int questionIndex) {
        return picked.get(questionIndex) != UNANSWERED;
    }

    public boolean isLastQuestion() {
        return index == questions.size() - 1;
    }

    // ----- Commands -----
    // Records one choice; later clicks for the same question are ignored.
    public void answer(int choice) {
        if (isAnswered(index)) {
            return;
        }
        picked.set(index, choice);
    }

    // Advances to the next question; does nothing on the last one.
    public void next() {
        if (!isLastQuestion()) {
            index = index + 1;
        }
    }

    // Marks every unanswered question as skipped.
    public void timeout() {
        for (int i = 0; i < picked.size(); i++) {
            if (picked.get(i) == UNANSWERED) {
                picked.set(i, SKIPPED);
            }
        }
    }

    // Grades the round; unanswered questions count as skipped.
    public MiniGameResult score(int elapsedSeconds) {
        timeout();
        return scorer.score(gameName, Lists.copyOf(questions), getAnswers(), elapsedSeconds);
    }

    // ----- Object overrides -----
    @Override
    public String toString() {
        return "QuizRound [" + gameName + ", Q" + (index + 1) + "/" + questions.size() + "]";
    }
}

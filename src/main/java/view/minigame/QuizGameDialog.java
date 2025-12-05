package view.minigame;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import javax.swing.*;

import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;
import model.minigame.QuizRound;
import model.minigame.ScoreStrategy;
import utils.Lists;
import utils.Require;
import view.theme.AppTheme;

// The quiz game screen. All round state lives in QuizRound; this class
// only draws the questions and forwards the player's clicks.
public class QuizGameDialog extends JDialog {

    private final String gameName;
    private final List<QuizQuestion> bank;
    private final ScoreStrategy scorer;

    private JLabel promptLabel;
    private JPanel optionsPanel;

    private JLabel feedbackLabel;
    private JButton nextButton;
    private JProgressBar timeBar;

    private QuizRound round;
    private final ElapsedTimer stopwatch;
    private javax.swing.Timer tick;
    private MiniGameResult result;
    private boolean accepted;

    QuizGameDialog(JFrame parent, String gameName,
                          List<QuizQuestion> bank, ScoreStrategy scorer,
                          Supplier<Long> clock) {
        super(parent, gameName, true);
        this.gameName = gameName;
        this.bank = Lists.copyOf(bank);
        this.scorer = Require.notNull(scorer, "scorer");
        this.stopwatch = new ElapsedTimer(Require.notNull(clock, "clock"));
        AppTheme.styleDialog(this, 600, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
    }

    /** Blocking play: returns the submitted result, or null on pre-finish cancel. */
    public MiniGameResult showAndPlay() {
        if (bank.isEmpty()) {
            return null;
        }
        startRound();
        setVisible(true);
        return accepted ? result : null;
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(AppTheme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        timeBar = new JProgressBar(0, ScoreStrategy.ROUND_PAR_SECONDS);
        timeBar.setStringPainted(true);
        timeBar.setForeground(AppTheme.PRIMARY_COLOR);
        timeBar.setBackground(AppTheme.DIVIDER_COLOR);
        root.add(timeBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(AppTheme.BACKGROUND_COLOR);
        promptLabel = new JLabel();
        promptLabel.setFont(AppTheme.FONT_SUBHEADING);
        promptLabel.setForeground(AppTheme.TEXT_PRIMARY);
        center.add(promptLabel, BorderLayout.NORTH);

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        center.add(new JScrollPane(optionsPanel), BorderLayout.CENTER);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(AppTheme.FONT_BODY);
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        center.add(feedbackLabel, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(AppTheme.BACKGROUND_COLOR);
        JButton skip = AppTheme.createSecondaryButton("Skip");
        skip.addActionListener(e -> answer(QuizRound.SKIPPED));
        nextButton = AppTheme.createPrimaryButton("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> next());
        bottom.add(skip, BorderLayout.WEST);
        bottom.add(nextButton, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        add(root, BorderLayout.CENTER);
    }

    private void startRound() {
        round = QuizRound.random(gameName, bank, QuizRound.QUESTIONS_PER_ROUND,
                new Random(), scorer);
        stopwatch.start();
        stopTick();
        tick = new javax.swing.Timer(200, e -> {
            int left = ScoreStrategy.ROUND_PAR_SECONDS - elapsedSeconds();
            if (left < 0) {
                left = 0;
            }
            timeBar.setMaximum(ScoreStrategy.ROUND_PAR_SECONDS);
            timeBar.setValue(left);
            timeBar.setString(left + "s left - Q" + (round.getIndex() + 1) + "/" + round.size());
            if (left <= 15) {
                timeBar.setForeground(AppTheme.ERROR_COLOR);
            } else {
                timeBar.setForeground(AppTheme.PRIMARY_COLOR);
            }
            if (left <= 0) {
                finish();
            }
        });
        tick.start();
        showQuestion();
    }

    private int elapsedSeconds() {
        return stopwatch.elapsedSeconds();
    }

    private void showQuestion() {
        QuizQuestion question = round.getCurrentQuestion();
        promptLabel.setText("<html><b>Q" + (round.getIndex() + 1) + ".</b> "
                + question.getPrompt() + "</html>");
        optionsPanel.removeAll();
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            final int choice = i;
            JButton button = AppTheme.createSecondaryButton(options.get(i));
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.addActionListener(e -> answer(choice));
            optionsPanel.add(button);
            optionsPanel.add(Box.createVerticalStrut(8));
        }
        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        nextButton.setEnabled(false);
        nextButton.setText(round.isLastQuestion() ? "Finish" : "Next");
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void answer(int choice) {
        if (round.isAnswered(round.getIndex())) {
            return;
        }
        round.answer(choice);
        QuizQuestion question = round.getCurrentQuestion();
        boolean correct = choice >= 0 && question.isCorrect(choice);

        for (int i = 0; i < optionsPanel.getComponents().length; i++) {
            Component component = optionsPanel.getComponents()[i];
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }

        feedbackLabel.setText(feedbackText(question, choice, correct));
        if (correct) {
            feedbackLabel.setForeground(AppTheme.SUCCESS_COLOR.darker());
        } else {
            feedbackLabel.setForeground(AppTheme.ERROR_COLOR);
        }
        nextButton.setEnabled(true);
        nextButton.setText(round.isLastQuestion() ? "Finish" : "Next");
    }

    // Presentation wording only; the model decides what is correct.
    private static String feedbackText(QuizQuestion question, int choice, boolean correct) {
        String answer = question.getOptions().get(question.getCorrectIndex());
        if (correct) {
            return "Correct! " + question.getExplanation();
        }
        if (question.getExplanation().equals("")) {
            return "Wrong. Answer was: " + answer;
        }
        String start = choice < 0 ? "Skipped. " : "Not quite. ";
        return start + question.getExplanation() + " (was: " + answer + ")";
    }

    private void next() {
        if (!round.isLastQuestion()) {
            round.next();
            showQuestion();
        } else {
            finish();
        }
    }

    private void finish() {
        stopTick();
        int elapsed = elapsedSeconds();
        if (elapsed < 1) {
            elapsed = 1;
        }
        result = round.score(elapsed);

        String highlights = String.join(" - ", result.getHighlights());
        String pass = result.isPassed() ? "PASS" : "FAIL";
        String html = "<html><center><h2>Score " + result.getScore() + "/100 " + pass + "</h2>"
                + highlights + "<br>" + result.getElapsedSeconds() + "s</center></html>";
        int type = result.isPassed() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
        JOptionPane.showMessageDialog(this, html, "Result — " + gameName, type);
        accepted = true;
        dispose();
    }

    private void stopTick() {
        if (tick != null && tick.isRunning()) {
            tick.stop();
        }
    }

    // Closing the window cancels the round and stops the repaint timer.
    @Override
    public void dispose() {
        stopTick();
        super.dispose();
    }
}

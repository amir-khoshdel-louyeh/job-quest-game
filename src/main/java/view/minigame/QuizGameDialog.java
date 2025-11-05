package view.minigame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;
import services.minigame.QuizScorer;
import view.theme.AppTheme;

/**
 * Quiz mini-game dialog: Doctors diagnose, Designers answer client briefs.
 *
 * <p>Pure view (SRP): asks 3 random questions one at a time with a round
 * timer, grading lives in {@link QuizScorer}. Modal — call
 * {@link #showAndPlay()} and get a {@link MiniGameResult} or {@code null}
 * when the player cancels.</p>
 */
public class QuizGameDialog extends JDialog {

    private final String gameName;
    private final List<QuizQuestion> bank;
    private final QuizScorer scorer;

    private JLabel promptLabel;
    private JPanel optionsPanel;
    private JLabel feedbackLabel;
    private JButton nextButton;
    private JProgressBar timeBar;

    private List<QuizQuestion> round;
    private List<Integer> picked;
    private int index;
    private long startMillis;
    private javax.swing.Timer tick;
    private MiniGameResult result;
    private boolean accepted;

    public QuizGameDialog(JFrame parent, String gameName,
                          List<QuizQuestion> bank, QuizScorer scorer) {
        super(parent, gameName, true);
        this.gameName = gameName;
        this.bank = bank;
        this.scorer = scorer != null ? scorer : new QuizScorer();
        AppTheme.styleDialog(this, 600, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
    }

    /** Blocking play: returns the accepted result, or null on cancel. */
    public MiniGameResult showAndPlay() {
        startRound();
        setVisible(true);
        return accepted ? result : null;
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(AppTheme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        timeBar = new JProgressBar(0, QuizScorer.ROUND_PAR_SECONDS);
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
        JButton skip = AppTheme.createSecondaryButton("Skip ⏭");
        skip.addActionListener(e -> answer(-1));
        nextButton = AppTheme.createPrimaryButton("Next →");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> next());
        bottom.add(skip, BorderLayout.WEST);
        bottom.add(nextButton, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        add(root, BorderLayout.CENTER);
    }

    private void startRound() {
        List<QuizQuestion> shuffled = new ArrayList<>(bank);
        Collections.shuffle(shuffled);
        round = shuffled.subList(0, Math.min(3, shuffled.size()));
        picked = new ArrayList<>(Collections.nCopies(round.size(), -2));
        index = 0;
        startMillis = System.currentTimeMillis();
        stopTick();
        tick = new javax.swing.Timer(200, e -> {
            int elapsed = elapsedSeconds();
            int left = Math.max(0, QuizScorer.ROUND_PAR_SECONDS - elapsed);
            timeBar.setMaximum(QuizScorer.ROUND_PAR_SECONDS);
            timeBar.setValue(left);
            timeBar.setString("⏱ " + left + "s left · Q" + (index + 1) + "/" + round.size());
            timeBar.setForeground(left <= 15 ? AppTheme.ERROR_COLOR : AppTheme.PRIMARY_COLOR);
            if (left <= 0) {
                // timeout: mark remaining as skipped, finish
                for (int i = 0; i < picked.size(); i++) {
                    if (picked.get(i) == -2) picked.set(i, -1);
                }
                finish();
            }
        });
        tick.start();
        showQuestion();
    }

    private int elapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startMillis) / 1000);
    }

    private void showQuestion() {
        QuizQuestion q = round.get(index);
        promptLabel.setText("<html><b>Q" + (index + 1) + ".</b> " + q.getPrompt() + "</html>");
        optionsPanel.removeAll();
        List<String> opts = q.getOptions();
        for (int i = 0; i < opts.size(); i++) {
            final int choice = i;
            JButton b = AppTheme.createSecondaryButton(opts.get(i));
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.addActionListener(e -> answer(choice));
            optionsPanel.add(b);
            optionsPanel.add(Box.createVerticalStrut(8));
        }
        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(AppTheme.TEXT_SECONDARY);
        nextButton.setEnabled(false);
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void answer(int choice) {
        if (picked.get(index) != -2) return; // already answered
        picked.set(index, choice);
        QuizQuestion q = round.get(index);
        boolean ok = choice >= 0 && q.isCorrect(choice);
        // lock buttons, highlight correct one
        for (Component c : optionsPanel.getComponents()) {
            if (c instanceof JButton b) b.setEnabled(false);
        }
        feedbackLabel.setText(ok ? "✅ Correct! " + q.getExplanation()
                : (choice < 0 ? "⏭ Skipped. " : "❌ Not quite. ")
                        + (q.getExplanation().isEmpty() ? "" : q.getExplanation()
                        + " (was: " + q.getOptions().get(q.getCorrectIndex()) + ")"));
        feedbackLabel.setForeground(ok ? AppTheme.SUCCESS_COLOR.darker() : AppTheme.ERROR_COLOR);
        nextButton.setEnabled(true);
        if (index == round.size() - 1) nextButton.setText("Finish ✔");
    }

    private void next() {
        if (index < round.size() - 1) {
            index++;
            showQuestion();
        } else {
            finish();
        }
    }

    private void finish() {
        stopTick();
        // any unanswered (shouldn't happen) counts as skip
        for (int i = 0; i < picked.size(); i++) {
            if (picked.get(i) == -2) picked.set(i, -1);
        }
        result = scorer.score(gameName, round, picked, Math.max(1, elapsedSeconds()));
        String html = String.format(
                "<html><center><h2>Score %d/100 %s</h2>%s<br>%ds</center></html>",
                result.getScore(), result.isPassed() ? "✅" : "❌",
                String.join(" · ", result.getHighlights()), result.getElapsedSeconds());
        int choice = JOptionPane.showOptionDialog(this, html, "Result — " + gameName,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"✔ Accept (x" + String.format("%.2f", result.rewardMultiplier()) + " pay)",
                        "↻ Retry", "✖ Cancel job"}, "✔ Accept");
        if (choice == 0) {
            accepted = true;
            dispose();
        } else if (choice == 1) {
            result = null;
            nextButton.setText("Next →");
            startRound();
        } else {
            result = null;
            accepted = false;
            dispose();
        }
    }

    private void stopTick() {
        if (tick != null && tick.isRunning()) tick.stop();
    }
}

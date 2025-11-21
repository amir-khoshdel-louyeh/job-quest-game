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

// The quiz game: answer 3 questions before time runs out.
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
    private final utils.ElapsedTimer stopwatch = new utils.ElapsedTimer();
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

    /** Blocking play: returns the submitted result, or null on pre-finish cancel. */
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
        JButton skip = AppTheme.createSecondaryButton("Skip");
        skip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                answer(-1);
            }
        });
        nextButton = AppTheme.createPrimaryButton("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                next();
            }
        });
        bottom.add(skip, BorderLayout.WEST);
        bottom.add(nextButton, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        add(root, BorderLayout.CENTER);
    }

    private void startRound() {
        ArrayList<QuizQuestion> shuffled = new ArrayList<QuizQuestion>(bank);
        Collections.shuffle(shuffled);
        int count = 3;
        if (shuffled.size() < count) {
            count = shuffled.size();
        }
        round = new ArrayList<QuizQuestion>();
        for (int i = 0; i < count; i++) {
            round.add(shuffled.get(i));
        }
        picked = new ArrayList<Integer>();
        for (int i = 0; i < round.size(); i++) {
            picked.add(-2);
        }
        index = 0;
        stopwatch.start();
        stopTick();
        tick = new javax.swing.Timer(200, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int elapsed = elapsedSeconds();
                int left = QuizScorer.ROUND_PAR_SECONDS - elapsed;
                if (left < 0) {
                    left = 0;
                }
                timeBar.setMaximum(QuizScorer.ROUND_PAR_SECONDS);
                timeBar.setValue(left);
                timeBar.setString(left + "s left - Q" + (index + 1) + "/" + round.size());
                if (left <= 15) {
                    timeBar.setForeground(AppTheme.ERROR_COLOR);
                } else {
                    timeBar.setForeground(AppTheme.PRIMARY_COLOR);
                }
                if (left <= 0) {
                    for (int i = 0; i < picked.size(); i++) {
                        if (picked.get(i) == -2) {
                            picked.set(i, -1);
                        }
                    }
                    finish();
                }
            }
        });
        tick.start();
        showQuestion();
    }

    private int elapsedSeconds() {
        return stopwatch.elapsedSeconds();
    }

    private void showQuestion() {
        QuizQuestion q = round.get(index);
        promptLabel.setText("<html><b>Q" + (index + 1) + ".</b> " + q.getPrompt() + "</html>");
        optionsPanel.removeAll();
        List<String> opts = q.getOptions();
        for (int i = 0; i < opts.size(); i++) {
            final int choice = i;
            String label = opts.get(i);
            JButton b = AppTheme.createSecondaryButton(label);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    answer(choice);
                }
            });
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
        if (picked.get(index) != -2) {
            return;
        }
        picked.set(index, choice);
        QuizQuestion q = round.get(index);
        boolean ok = false;
        if (choice >= 0 && q.isCorrect(choice)) {
            ok = true;
        }
        for (int i = 0; i < optionsPanel.getComponents().length; i++) {
            Component c = optionsPanel.getComponents()[i];
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                b.setEnabled(false);
            }
        }
        String msg = "";
        if (ok) {
            msg = "Correct! " + q.getExplanation();
        } else {
            if (choice < 0) {
                msg = "Skipped. ";
            } else {
                msg = "Not quite. ";
            }
            msg = msg + q.getExplanation();
            if (q.getExplanation().equals("")) {
                msg = "Wrong. Answer was: " + q.getOptions().get(q.getCorrectIndex());
            } else {
                msg = msg + " (was: " + q.getOptions().get(q.getCorrectIndex()) + ")";
            }
        }
        feedbackLabel.setText(msg);
        if (ok) {
            feedbackLabel.setForeground(AppTheme.SUCCESS_COLOR.darker());
        } else {
            feedbackLabel.setForeground(AppTheme.ERROR_COLOR);
        }
        nextButton.setEnabled(true);
        if (index == round.size() - 1) {
            nextButton.setText("Finish");
        }
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
        for (int i = 0; i < picked.size(); i++) {
            if (picked.get(i) == -2) {
                picked.set(i, -1);
            }
        }
        int elapsed = elapsedSeconds();
        if (elapsed < 1) {
            elapsed = 1;
        }
        result = scorer.score(gameName, round, picked, elapsed);
        String highlights = "";
        List<String> hl = result.getHighlights();
        for (int i = 0; i < hl.size(); i++) {
            if (i > 0) {
                highlights = highlights + " - ";
            }
            highlights = highlights + hl.get(i);
        }
        String pass = "FAIL";
        if (result.isPassed()) {
            pass = "PASS";
        }
        String html = "<html><center><h2>Score " + result.getScore() + "/100 " + pass + "</h2>"
                + highlights + "<br>" + result.getElapsedSeconds() + "s</center></html>";
        int type = JOptionPane.INFORMATION_MESSAGE;
        if (!result.isPassed()) {
            type = JOptionPane.WARNING_MESSAGE;
        }
        JOptionPane.showMessageDialog(this, html, "Result — " + gameName, type);
        accepted = true;
        dispose();
    }

    private void stopTick() {
        if (tick != null && tick.isRunning()) tick.stop();
    }
}

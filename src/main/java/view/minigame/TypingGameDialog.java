package view.minigame;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import model.minigame.MiniGameResult;
import model.minigame.TextTask;
import services.minigame.TypingScorer;
import view.theme.AppTheme;

/**
 * Retype mini-game dialog: Typists copy prose, Programmers copy tiny programs.
 *
 * <p>Pure view (SRP): measures time and collects input, grading lives in
 * {@link TypingScorer} (DIP, injectable for tests). Modal — call
 * {@link #showAndPlay()} and get a {@link MiniGameResult} or {@code null}
 * when the player cancels (no penalty then).</p>
 */
public class TypingGameDialog extends JDialog {

    private final String gameName;
    private final List<TextTask> tasks;
    private final TypingScorer scorer;
    private final boolean codeMode;
    /** When true the dialog is locked to one job's challenge — no second picker. */
    private final boolean lockedToJob;

    private JComboBox<TextTask> taskBox;
    private JTextArea targetArea;
    private JTextArea inputArea;
    private JButton startButton;
    private JButton submitButton;
    private JProgressBar timeBar;
    private JLabel statusLabel;

    private TextTask current;
    private long startMillis;
    private javax.swing.Timer tick;
    private MiniGameResult result;
    private boolean accepted;

    /**
     * @param codeMode true → monospace target + "Run it" wording for programmers
     */
    public TypingGameDialog(JFrame parent, String gameName, List<TextTask> tasks,
                            TypingScorer scorer, boolean codeMode) {
        super(parent, gameName, true);
        this.gameName = gameName;
        this.tasks = tasks;
        this.scorer = scorer != null ? scorer : new TypingScorer();
        this.codeMode = codeMode;
        this.lockedToJob = false;
        AppTheme.styleDialog(this, 620, 640);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
    }

    /**
     * Job-locked mode: the challenge IS the selected job's work, so there is
     * no second "Pick a task" dropdown. Title should already contain the job
     * name (e.g. "Fix a CSS Bug — Write the Program").
     */
    public TypingGameDialog(JFrame parent, String gameName, TextTask jobTask,
                            TypingScorer scorer, boolean codeMode) {
        super(parent, gameName, true);
        if (jobTask == null) throw new IllegalArgumentException("jobTask required");
        this.gameName = gameName;
        this.tasks = List.of(jobTask);
        this.scorer = scorer != null ? scorer : new TypingScorer();
        this.codeMode = codeMode;
        this.lockedToJob = true;
        AppTheme.styleDialog(this, 620, 640);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
    }

    /** Blocking play: returns the accepted result, or null on cancel. */
    public MiniGameResult showAndPlay() {
        setVisible(true);
        return accepted ? result : null;
    }

    private void buildUi() {
        setLayout(new BorderLayout(10, 10));
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(AppTheme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ---- picker row ----
        // Job-locked mode: no second dropdown — the Workshop's job pick IS the
        // selection. Show the fixed challenge name instead of "Pick a task".
        JPanel pick = new JPanel(new BorderLayout(8, 8));
        pick.setBackground(AppTheme.BACKGROUND_COLOR);
        startButton = AppTheme.createPrimaryButton("▶ Start");
        startButton.addActionListener(e -> startRound());
        if (lockedToJob) {
            TextTask only = tasks.get(0);
            JLabel fixed = new JLabel(only.getTitle() + "  " + only.difficultyStars());
            fixed.setFont(AppTheme.FONT_BODY);
            pick.add(fixed, BorderLayout.CENTER);
        } else {
            taskBox = new JComboBox<>(tasks.toArray(new TextTask[0]));
            taskBox.setFont(AppTheme.FONT_BODY);
            taskBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> l, Object v, int i,
                                                              boolean sel, boolean foc) {
                    super.getListCellRendererComponent(l, v, i, sel, foc);
                    if (v instanceof TextTask t) {
                        setText(t.getTitle() + "  " + t.difficultyStars());
                    }
                    return this;
                }
            });
            taskBox.addActionListener(e -> previewSelected());
            pick.add(new JLabel("Pick a challenge:"), BorderLayout.WEST);
            pick.add(taskBox, BorderLayout.CENTER);
        }
        pick.add(startButton, BorderLayout.EAST);
        root.add(pick, BorderLayout.NORTH);

        // ---- center: target + input ----
        JPanel center = new JPanel(new GridLayout(2, 1, 8, 8));
        center.setBackground(AppTheme.BACKGROUND_COLOR);
        targetArea = new JTextArea();
        targetArea.setEditable(false);
        targetArea.setLineWrap(true);
        targetArea.setWrapStyleWord(!codeMode);
        targetArea.setFont(codeMode
                ? new Font(Font.MONOSPACED, Font.BOLD, 14)
                : AppTheme.FONT_BODY);
        targetArea.setBackground(AppTheme.SURFACE_COLOR);
        inputArea = new JTextArea();
        inputArea.setEnabled(false);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(!codeMode);
        inputArea.setFont(codeMode
                ? new Font(Font.MONOSPACED, Font.PLAIN, 14)
                : AppTheme.FONT_BODY);
        inputArea.setBackground(AppTheme.SURFACE_COLOR);
        center.add(labeled("📋 Copy this:", new JScrollPane(targetArea)));
        center.add(labeled(codeMode ? "⌨️ Write the program here:" : "⌨️ Type it here:", new JScrollPane(inputArea)));
        root.add(center, BorderLayout.CENTER);

        // ---- bottom: timer + status + submit ----
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBackground(AppTheme.BACKGROUND_COLOR);
        timeBar = new JProgressBar(0, 100);
        timeBar.setStringPainted(true);
        timeBar.setString(lockedToJob ? "Press Start to begin this job's work" : "Pick a challenge and press Start");
        timeBar.setForeground(AppTheme.PRIMARY_COLOR);
        timeBar.setBackground(AppTheme.DIVIDER_COLOR);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(AppTheme.FONT_SMALL);
        statusLabel.setForeground(AppTheme.TEXT_SECONDARY);
        submitButton = AppTheme.createSuccessButton(codeMode ? "▶ Run it" : "✔ Submit");
        submitButton.setEnabled(false);
        submitButton.addActionListener(e -> submit());
        bottom.add(timeBar, BorderLayout.NORTH);
        bottom.add(statusLabel, BorderLayout.CENTER);
        bottom.add(submitButton, BorderLayout.EAST);
        root.add(bottom, BorderLayout.SOUTH);

        add(root, BorderLayout.CENTER);
        previewSelected();
    }

    private JPanel labeled(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(AppTheme.BACKGROUND_COLOR);
        JLabel l = new JLabel(title);
        l.setFont(AppTheme.FONT_SUBHEADING);
        l.setForeground(AppTheme.PRIMARY_COLOR);
        p.add(l, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void previewSelected() {
        TextTask t = lockedToJob ? tasks.get(0) : (TextTask) taskBox.getSelectedItem();
        if (t == null) return;
        targetArea.setText(t.getContent());
        statusLabel.setText(String.format("%s · par %ds — exact copy wins, speed gives bonus",
                t.difficultyStars(), t.getParSeconds()));
    }

    private void startRound() {
        current = lockedToJob ? tasks.get(0) : (TextTask) taskBox.getSelectedItem();
        if (current == null) return;
        inputArea.setText("");
        inputArea.setEnabled(true);
        inputArea.requestFocus();
        if (taskBox != null) taskBox.setEnabled(false);
        startButton.setEnabled(false);
        submitButton.setEnabled(true);
        startMillis = System.currentTimeMillis();
        stopTick();
        int limit = current.getParSeconds() * 3;
        tick = new javax.swing.Timer(200, e -> {
            int elapsed = elapsedSeconds();
            int left = Math.max(0, limit - elapsed);
            timeBar.setMaximum(limit);
            timeBar.setValue(left);
            timeBar.setString(left + "s left");
            timeBar.setForeground(left <= 10 ? AppTheme.ERROR_COLOR : AppTheme.PRIMARY_COLOR);
            if (left <= 0) submit();
        });
        tick.start();
    }

    private int elapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startMillis) / 1000);
    }

    private void submit() {
        if (current == null || result != null && accepted) return;
        stopTick();
        int elapsed = Math.max(1, elapsedSeconds());
        result = scorer.score(gameName, current, inputArea.getText(), elapsed);
        inputArea.setEnabled(false);
        submitButton.setEnabled(false);
        showResult();
    }

    private void stopTick() {
        if (tick != null && tick.isRunning()) tick.stop();
    }

    private void showResult() {
        String html = String.format(
                "<html><center><h2>Score %d/100 %s</h2>"
                        + "Accuracy %d%% · %s · %ds<br>%s</center></html>",
                result.getScore(), result.isPassed() ? "✅" : "❌",
                result.getAccuracy(),
                result.getWpm() > 0 ? result.getWpm() + " WPM" : "",
                result.getElapsedSeconds(),
                result.summaryLine().replace("🎮 ", ""));
        int choice = JOptionPane.showOptionDialog(this, html, "Result — " + gameName,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{acceptLabel(), "↻ Retry", "✖ Cancel job"}, "✔ Accept");
        if (choice == 0) {
            accepted = true;
            dispose();
        } else if (choice == 1) {
            result = null;
            if (taskBox != null) taskBox.setEnabled(true);
            startButton.setEnabled(true);
            statusLabel.setText(lockedToJob ? "Press Start to retry this job's work" : "Pick a challenge (or the same one) and press Start");
            timeBar.setValue(timeBar.getMaximum());
            timeBar.setString("Ready");
        } else {
            result = null;
            accepted = false;
            dispose();
        }
    }

    private String acceptLabel() {
        // Fail state is visible before accepting: below 50 means no pay and
        // the job's energy is lost, so the button says so (Retry stays free).
        if (!result.isPassed()) {
            return "✔ Accept (FAIL — no pay, energy lost)";
        }
        return "✔ Accept (" + payHint() + ")";
    }

    private String payHint() {
        // preview of the multiplier at the achieved score
        double m = result.rewardMultiplier();
        return String.format("x%.2f pay", m);
    }
}

package view;

import controller.GameController;
import model.Job;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A dialog window that displays a list of available jobs for the user.
 */
public class JobDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;

    // Dialog that lists available jobs for the user
    public JobDialog(JFrame parent, GameController controller, GamePanel gamePanel, List<Job> jobs) {
        super(parent, "Available Jobs", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10));
        // Step-5: shared dialog look + themed empty state
        AppTheme.styleDialog(this, 500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (jobs.isEmpty()) {
            JLabel empty = new JLabel("No jobs available for your current identity and skill level. Learn more skills!", SwingConstants.CENTER);
            empty.setFont(AppTheme.FONT_BODY);
            empty.setForeground(AppTheme.TEXT_SECONDARY);
            add(empty);
            return;
        }

        JPanel jobsContainer = new JPanel();
        jobsContainer.setLayout(new BoxLayout(jobsContainer, BoxLayout.Y_AXIS));
        jobsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jobsContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        for (Job job : jobs) {
            jobsContainer.add(createJobPanel(job));
            jobsContainer.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(jobsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Create a single job entry panel with action button
    private JPanel createJobPanel(Job job) {
        // Step-5: one card factory — no more duplicated CompoundBorder + Arial
        JPanel panel = AppTheme.createItemCard();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = AppTheme.createCardTitle(job.getName());
        infoPanel.add(nameLabel);

        JLabel descriptionLabel = AppTheme.createCardDescription(job.getDescription());
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        String rewardText = String.format("Pay: $%d | Energy: -%d", job.getPayment(), job.getEnergyCost());
        JLabel rewardLabel = new JLabel(rewardText);
        rewardLabel.setFont(AppTheme.FONT_BUTTON);
        rewardLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(rewardLabel, BorderLayout.CENTER);

        JButton doItButton = AppTheme.createSuccessButton("Do It!");
        // When clicked, attempt the job via the controller and show the result.
        doItButton.addActionListener(e -> handleDoJob(job, doItButton));
        actionPanel.add(doItButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Small helper that executes the job and shows messages in a consistent way.
    // Step-9 UX: every job first plays the identity's work mini-game
    // (Typist retypes · Programmer writes a tiny program · Doctor diagnoses ·
    // Designer answers the brief). Cancel = no job, no penalty. The game score
    // scales pay/XP via GameController.doJob(job, game).
    private void handleDoJob(Job job, JButton source) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        String identity = gamePanel.getUserIdentity();
        GameController.ActionResult result;
        if (isGameIdentity(identity)) {
            // Single-pick flow: the job chosen above IS the work —
            // the mini-game opens locked to that job, no second picker.
            model.minigame.MiniGameResult game = playWorkGame(parent, identity, job);
            if (game == null) {
                return; // cancelled — nothing happens, dialog stays open
            }
            result = controller.doJob(job, game);
        } else {
            result = controller.doJob(job); // unknown identity → legacy path
        }
        gamePanel.addChatMessage(result.message);
        gamePanel.updateUserInfo();
        if (!result.success) {
            JOptionPane.showMessageDialog(this, result.message, "Cannot Work", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static boolean isGameIdentity(String identity) {
        return "Typist".equals(identity) || "Programmer".equals(identity)
                || "Doctor".equals(identity) || "LogoDesigner".equals(identity);
    }

    /**
     * Strategy pick by identity (Strategy pattern): each profession plays its
     * own game, the dialog only orchestrates (SRP).
     * Single-pick: the given job determines the challenge content and the
     * window title, so "Fix a CSS Bug" opens "Fix a CSS Bug — Write the
     * Program" locked to that job instead of a generic second task list.
     *
     * @return accepted game result, or null when cancelled
     */
    private model.minigame.MiniGameResult playWorkGame(JFrame parent, String identity, Job job) {
        String jobName = job != null ? job.getName() : "Job";
        return switch (identity) {
            case "Typist" -> new view.minigame.TypingGameDialog(
                    parent, jobName + " — Typing Sprint",
                    provider.TypingTaskProvider.getTaskForJob(jobName), null, false)
                    .showAndPlay();
            case "Programmer" -> new view.minigame.TypingGameDialog(
                    parent, jobName + " — Write the Program",
                    provider.CodeTaskProvider.getTaskForJob(jobName), null, true)
                    .showAndPlay();
            case "Doctor" -> new view.minigame.QuizGameDialog(
                    parent, jobName + " — Diagnosis Round", provider.QuizProvider.getDiagnosisQuestions(), null)
                    .showAndPlay();
            case "LogoDesigner" -> new view.minigame.QuizGameDialog(
                    parent, jobName + " — Client Brief Round", provider.QuizProvider.getDesignQuestions(), null)
                    .showAndPlay();
            default -> null;
        };
    }
}
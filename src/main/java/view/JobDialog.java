package view;

import controller.GameController;
import controller.Outcome;
import model.career.Job;
import model.career.SkillSet;
import model.minigame.MiniGameResult;
import model.player.PlayerView;
import view.minigame.MiniGameLauncher;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// The workshop screen: do your career's jobs, locked ones show missing skills.
public class JobDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;
    private final MiniGameLauncher workGame;

    public JobDialog(JFrame parent, GameController controller, GamePanel gamePanel,
                     List<Job> allJobs, PlayerView player, MiniGameLauncher workGame) {
        super(parent, "Jobs", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        this.workGame = utils.Require.notNull(workGame, "workGame");
        String userIdentity = player.getIdentityName();
        setTitle("Workshop — " + userIdentity + " Jobs");
        setLayout(new BorderLayout(10, 10));
        AppTheme.styleDialog(this, 500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (allJobs.isEmpty()) {
            JLabel empty = new JLabel("No jobs exist yet.", SwingConstants.CENTER);
            empty.setFont(AppTheme.FONT_BODY);
            empty.setForeground(AppTheme.TEXT_SECONDARY);
            add(empty);
            return;
        }

        SkillSet skills = player.getSkillSet();

        // Available first, locked after; the controller already sent this career's jobs.
        List<Job> available = new java.util.ArrayList<>();
        List<Job> locked = new java.util.ArrayList<>();
        for (Job job : allJobs) {
            if (job.isAvailableFor(player.getIdentity(), skills)) {
                available.add(job);
            } else {
                locked.add(job);
            }
        }

        JLabel hint = new JLabel("Do available jobs now — locked ones show what 🎓 School skills unlock.",
                SwingConstants.CENTER);
        hint.setFont(AppTheme.FONT_SMALL);
        hint.setForeground(AppTheme.TEXT_SECONDARY);
        hint.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
        add(hint, BorderLayout.NORTH);

        JPanel jobsContainer = new JPanel();
        jobsContainer.setLayout(new BoxLayout(jobsContainer, BoxLayout.Y_AXIS));
        jobsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jobsContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        for (Job job : available) {
            jobsContainer.add(createJobPanel(job, true, null));
            jobsContainer.add(Box.createVerticalStrut(10));
        }
        if (!available.isEmpty() && !locked.isEmpty()) {
            JLabel lockedHeader = new JLabel("🔒 Locked");
            lockedHeader.setFont(AppTheme.FONT_SUBHEADING);
            lockedHeader.setForeground(AppTheme.TEXT_SECONDARY);
            jobsContainer.add(lockedHeader);
            jobsContainer.add(Box.createVerticalStrut(10));
        }
        for (Job job : locked) {
            jobsContainer.add(createJobPanel(job, false, lockReason(job, skills)));
            jobsContainer.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(jobsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    // View formats; the missing set is computed inside Job (single source).
    private static String lockReason(Job job, SkillSet skills) {
        List<String> missing = job.missingSkills(skills);
        if (missing.isEmpty()) {
            return "Not available yet";
        }
        String names = "";
        for (int i = 0; i < missing.size(); i++) {
            if (i > 0) {
                names = names + ", ";
            }
            names = names + missing.get(i);
        }
        return "Requires skills: " + names + " — learn at School";
    }

    private JPanel createJobPanel(Job job, boolean available, String lockReason) {
        JPanel panel = AppTheme.createItemCard();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        String title = job.getName();
        if (!available) {
            title = title + " (locked)";
        }
        JLabel nameLabel = AppTheme.createCardTitle(title);
        infoPanel.add(nameLabel);

        JLabel descriptionLabel = AppTheme.createCardDescription(job.getDescription());
        infoPanel.add(descriptionLabel);

        if (!available) {
            JLabel lockLabel = new JLabel("<html>" + lockReason + "</html>");
            lockLabel.setFont(AppTheme.FONT_SMALL);
            lockLabel.setForeground(AppTheme.ERROR_COLOR);
            infoPanel.add(lockLabel);
        }

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        String rewardText = "Pay: $" + job.getPayment() + " | Energy: -" + job.getEnergyCost();
        JLabel rewardLabel = new JLabel(rewardText);
        rewardLabel.setFont(AppTheme.FONT_BUTTON);
        rewardLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(rewardLabel, BorderLayout.CENTER);

        JButton doItButton = AppTheme.createSuccessButton("Do It!");
        doItButton.setEnabled(available);
        if (!available) {
            doItButton.setToolTipText(lockReason);
        } else {
            final Job myJob = job;
            doItButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    handleDoJob(myJob);
                }
            });
        }
        actionPanel.add(doItButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Every job first plays a short work game; the submitted score sets pay and XP.
    // Cancel before submitting = no job, no penalty.
    private void handleDoJob(Job job) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        MiniGameResult game = playWorkGame(parent, job);
        if (game == null) {
            return; // cancelled — nothing happens, dialog stays open
        }
        Outcome<?> result = controller.doJob(job, game);
        gamePanel.addChatMessage(result.getMessage());
        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Cannot Work", JOptionPane.WARNING_MESSAGE);
        }
    }

    // The injected launcher decides which mini-game a job plays.
    private MiniGameResult playWorkGame(JFrame parent, Job job) {
        String jobName = "Job";
        if (job != null) {
            jobName = job.getName();
        }
        return workGame.launch(parent, jobName);
    }
}
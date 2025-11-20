package view;

import controller.GameController;
import model.Identity;
import model.career.Job;
import model.player.User;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// The workshop screen: do your career's jobs, locked ones show missing skills.
public class JobDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;

    public JobDialog(JFrame parent, GameController controller, GamePanel gamePanel,
                     List<Job> allJobs, User user) {
        super(parent, "Jobs", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        String userIdentity = user.getIdentity().getDisplayName();
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

        List<String> userSkills = user.getSkills();

        // Available first, locked after.
        // Drop other careers' jobs if an unfiltered list arrives.
        List<Job> available = new java.util.ArrayList<>();
        List<Job> locked = new java.util.ArrayList<>();
        for (Job job : allJobs) {
            if (!job.isForIdentity(user.getIdentity())) {
                continue;
            }
            if (job.isAvailableFor(user.getIdentity(), userSkills)) {
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
            jobsContainer.add(createJobPanel(job, false, lockReason(job, userSkills)));
            jobsContainer.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(jobsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    private static String lockReason(Job job, List<String> userSkills) {
        List<String> missing = new java.util.ArrayList<String>();
        List<String> need = job.getRequiredSkills();
        for (int i = 0; i < need.size(); i++) {
            String s = need.get(i);
            if (!userSkills.contains(s)) {
                missing.add(s);
            }
        }
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
            final JButton myButton = doItButton;
            doItButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    handleDoJob(myJob, myButton);
                }
            });
        }
        actionPanel.add(doItButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Every job first plays a short work game; the submitted score sets pay and XP. Cancel before submitting = no job, no penalty.
    private void handleDoJob(Job job, JButton source) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        Identity identity = gamePanel.getUserIdentity();
        GameController.ActionResult result;
        if (identity != null) {
            // Single-pick flow: the job chosen above IS the work —
            // the mini-game opens locked to that job, no second picker.
            // Which game to open is asked from the profession itself,
            // so adding a new profession needs no change here
            // as long as it reuses an existing game kind.
            model.minigame.MiniGameResult game = playWorkGame(parent, identity, job);
            if (game == null) {
                return; // cancelled or unknown game kind — nothing happens, dialog stays open
            }
            result = controller.doJob(job, game);
        } else {
            result = controller.doJob(job); // unknown identity → legacy path
        }
        gamePanel.addChatMessage(result.getMessage());
        gamePanel.updateUserInfo();
        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Cannot Work", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Registry instead of a switch: a new WorkGameKind adds one entry,
    // the dialog itself never changes (OCP).
    private static final java.util.Map<model.WorkGameKind, view.minigame.MiniGameLauncher> LAUNCHERS =
            new java.util.EnumMap<>(model.WorkGameKind.class);
    static {
        LAUNCHERS.put(model.WorkGameKind.TYPING, new view.minigame.TypingLauncher());
        LAUNCHERS.put(model.WorkGameKind.CODE, new view.minigame.CodeLauncher());
        LAUNCHERS.put(model.WorkGameKind.DIAGNOSIS, new view.minigame.DiagnosisLauncher());
        LAUNCHERS.put(model.WorkGameKind.DESIGN, new view.minigame.DesignLauncher());
    }

    private model.minigame.MiniGameResult playWorkGame(JFrame parent, Identity identity, Job job) {
        String jobName = "Job";
        if (job != null) {
            jobName = job.getName();
        }
        model.WorkGameKind gameKind = identity.getWorkGameKind();
        if (gameKind == null) {
            return null;
        }
        view.minigame.MiniGameLauncher launcher = LAUNCHERS.get(gameKind);
        if (launcher == null) {
            return null;
        }
        return launcher.launch(parent, jobName);
    }
}
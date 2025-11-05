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
    // Step-5 UX: stays open after success for grinding (previously dispose()
    // forced reopen every job); result goes to chat + HUD via Observer.
    private void handleDoJob(Job job, JButton source) {
        GameController.ActionResult result = controller.doJob(job);
        gamePanel.addChatMessage(result.message);
        gamePanel.updateUserInfo();
        if (!result.success) {
            JOptionPane.showMessageDialog(this, result.message, "Cannot Work", JOptionPane.WARNING_MESSAGE);
        }
    }
}
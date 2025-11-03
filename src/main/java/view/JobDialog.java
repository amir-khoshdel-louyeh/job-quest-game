package view;

import controller.GameController;
import model.Job;

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
        setSize(500, 600);
        setLocationRelativeTo(parent);

        if (jobs.isEmpty()) {
            add(new JLabel("No jobs available for your current identity and skill level. Learn more skills!", SwingConstants.CENTER));
            return;
        }

        JPanel jobsContainer = new JPanel();
        jobsContainer.setLayout(new BoxLayout(jobsContainer, BoxLayout.Y_AXIS));
        jobsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(job.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(nameLabel);

        JLabel descriptionLabel = new JLabel("<html>" + job.getDescription() + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setForeground(Color.GRAY);
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        String rewardText = String.format("Pay: $%d | Energy: -%d", job.getPayment(), job.getEnergyCost());
        JLabel rewardLabel = new JLabel(rewardText);
        rewardLabel.setFont(new Font("Arial", Font.BOLD, 12));
        actionPanel.add(rewardLabel, BorderLayout.CENTER);

        JButton doItButton = new JButton("Do It!");
        doItButton.addActionListener(e -> {
            GameController.ActionResult result = controller.doJob(job);
            if (result.success) {
                // Show success message with all details
                JOptionPane.showMessageDialog(this, result.message, "Success!", JOptionPane.INFORMATION_MESSAGE);
                gamePanel.updateUserInfo();
                dispose(); // Close the dialog after successfully completing the job
            } else {
                JOptionPane.showMessageDialog(this, result.message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        actionPanel.add(doItButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }
}
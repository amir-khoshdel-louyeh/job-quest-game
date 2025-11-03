package view;

import controller.GameController;
import model.LearnableSkill;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A dialog window that displays a list of learnable skills.
 */
public class SkillDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;

    // Dialog showing learnable skills available to the user
    public SkillDialog(JFrame parent, GameController controller, GamePanel gamePanel, List<LearnableSkill> skills) {
        super(parent, "School of Skills", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        setLayout(new BorderLayout(10, 10));
        setSize(450, 500);
        setLocationRelativeTo(parent);

        JPanel skillsContainer = new JPanel();
        skillsContainer.setLayout(new BoxLayout(skillsContainer, BoxLayout.Y_AXIS));
        skillsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (skills.isEmpty()) {
            skillsContainer.add(new JLabel("No new skills available for you at this time. Play more to unlock!"));
        } else {
            for (LearnableSkill skill : skills) {
                skillsContainer.add(createSkillPanel(skill));
                skillsContainer.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(skillsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Create UI panel for a single learnable skill with purchase action
    private JPanel createSkillPanel(LearnableSkill skill) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(skill.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel descriptionLabel = new JLabel("<html>" + skill.getDescription() + "</html>"); // Use HTML for wrapping
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JButton learnButton = new JButton(String.format("Learn ($%d)", skill.getCost()));
        learnButton.setBackground(new Color(13, 110, 253)); // A blue color for learning
        learnButton.setForeground(Color.WHITE);
        learnButton.addActionListener(evt -> {
            GameController.ActionResult result = controller.learnSkill(skill.getName());
            if (result.success) {
                // Show success message with achievement and quest updates
                JOptionPane.showMessageDialog(this, result.message, "Skill Learned! 🎓", JOptionPane.INFORMATION_MESSAGE);
                gamePanel.updateUserInfo();
                dispose(); // Close dialog on success
            } else {
                JOptionPane.showMessageDialog(this, result.message, "Cannot Learn", JOptionPane.WARNING_MESSAGE);
            }
        });
        panel.add(learnButton, BorderLayout.EAST);

        return panel;
    }
}
package view;

import controller.GameController;
import model.LearnableSkill;
import view.theme.AppTheme;

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
        // Step-5: shared dialog look
        AppTheme.styleDialog(this, 450, 500);
        // Close this dialog when the user closes the window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel skillsContainer = new JPanel();
        skillsContainer.setLayout(new BoxLayout(skillsContainer, BoxLayout.Y_AXIS));
        skillsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        skillsContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        if (skills.isEmpty()) {
            JLabel empty = new JLabel("No new skills available for you at this time. Play more to unlock!");
            empty.setFont(AppTheme.FONT_BODY);
            empty.setForeground(AppTheme.TEXT_SECONDARY);
            skillsContainer.add(empty);
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
        JPanel panel = AppTheme.createItemCard();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = AppTheme.createCardTitle(skill.getName());

        JLabel descriptionLabel = AppTheme.createCardDescription(skill.getDescription()); // Use HTML for wrapping

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(descriptionLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        JButton learnButton = AppTheme.createPrimaryButton(String.format("Learn ($%d)", skill.getCost()));
        // Delegate the learning action to a small helper to keep this UI method short.
        learnButton.addActionListener(evt -> handleLearnSkill(skill));
        panel.add(learnButton, BorderLayout.EAST);

        return panel;
    }

    // Helper method to perform learning via controller and show results simply.
    // Step-5 UX: stays open after success (previously dispose() forced reopen);
    // learned skills disappear only when the dialog is reopened (School click).
    private void handleLearnSkill(LearnableSkill skill) {
        GameController.ActionResult result = controller.learnSkill(skill.getName());
        gamePanel.addChatMessage(result.message);
        gamePanel.updateUserInfo();
        if (!result.success) {
            JOptionPane.showMessageDialog(this, result.message, "Cannot Learn", JOptionPane.WARNING_MESSAGE);
        }
    }
}
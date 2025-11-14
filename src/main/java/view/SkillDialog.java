package view;

import controller.GameController;
import model.LearnableSkill;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// The school screen: learn your career's lessons, locked ones show playtime needed.
public class SkillDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;
    private final model.User user;
    private final List<LearnableSkill> allSkills;
    private final JPanel skillsContainer;

    public SkillDialog(JFrame parent, GameController controller, GamePanel gamePanel,
                       List<LearnableSkill> allSkills, model.User user) {
        super(parent, "School of Skills", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        this.allSkills = allSkills != null ? new ArrayList<>(allSkills) : new ArrayList<>();
        this.user = user;
        String userIdentity = user.getIdentity().getProfessionName();
        setTitle("School of Skills — " + userIdentity + " Lessons");
        setLayout(new BorderLayout(10, 10));
        AppTheme.styleDialog(this, 500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (this.allSkills.isEmpty()) {
            JLabel empty = new JLabel("No skills exist yet.", SwingConstants.CENTER);
            empty.setFont(AppTheme.FONT_BODY);
            empty.setForeground(AppTheme.TEXT_SECONDARY);
            add(empty);
            skillsContainer = null;
            return;
        }

        JLabel hint = new JLabel("Learn available skills now — locked ones show playtime needed to unlock.",
                SwingConstants.CENTER);
        hint.setFont(AppTheme.FONT_SMALL);
        hint.setForeground(AppTheme.TEXT_SECONDARY);
        hint.setBorder(BorderFactory.createEmptyBorder(8, 10, 0, 10));
        add(hint, BorderLayout.NORTH);

        skillsContainer = new JPanel();
        skillsContainer.setLayout(new BoxLayout(skillsContainer, BoxLayout.Y_AXIS));
        skillsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        skillsContainer.setBackground(AppTheme.BACKGROUND_COLOR);

        populateSkills();

        JScrollPane scrollPane = new JScrollPane(skillsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Available first, locked after, learned last.
    private void populateSkills() {
        skillsContainer.removeAll();

        long playMinutes = user.getTotalPlayTime() / (1000 * 60);
        List<LearnableSkill> available = new ArrayList<>();
        List<LearnableSkill> locked = new ArrayList<>();
        List<LearnableSkill> learned = new ArrayList<>();
        for (LearnableSkill skill : allSkills) {
            // Drop other careers' lessons if an unfiltered list arrives.
            if (!skill.isForIdentity(user.getIdentity())) {
                continue;
            }
            if (user.hasSkill(skill.getName())) {
                learned.add(skill);
            } else if (playMinutes >= skill.getRequiredPlayTimeMinutes()) {
                available.add(skill);
            } else {
                locked.add(skill);
            }
        }

        if (available.isEmpty() && locked.isEmpty() && !learned.isEmpty()) {
            JLabel done = new JLabel("You have learned every skill. Well done!", SwingConstants.CENTER);
            done.setFont(AppTheme.FONT_BODY);
            done.setForeground(AppTheme.TEXT_SECONDARY);
            skillsContainer.add(done);
            skillsContainer.add(Box.createVerticalStrut(10));
        }

        for (LearnableSkill skill : available) {
            skillsContainer.add(createSkillPanel(skill, SkillStatus.AVAILABLE, null));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        if (!available.isEmpty() && !locked.isEmpty()) {
            skillsContainer.add(sectionHeader("🔒 Locked"));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        for (LearnableSkill skill : locked) {
            skillsContainer.add(createSkillPanel(skill, SkillStatus.LOCKED,
                    lockReason(skill, playMinutes)));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        if (!learned.isEmpty()) {
            skillsContainer.add(sectionHeader("✓ Learned"));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        for (LearnableSkill skill : learned) {
            skillsContainer.add(createSkillPanel(skill, SkillStatus.LEARNED, "✓ Already learned"));
            skillsContainer.add(Box.createVerticalStrut(10));
        }

        skillsContainer.revalidate();
        skillsContainer.repaint();
    }

    private static JLabel sectionHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(AppTheme.FONT_SUBHEADING);
        header.setForeground(AppTheme.TEXT_SECONDARY);
        return header;
    }

    // Why locked: show needed playtime.
    private static String lockReason(LearnableSkill skill, long playMinutes) {
        long need = skill.getRequiredPlayTimeMinutes();
        long remaining = need - playMinutes;
        if (remaining < 0) {
            remaining = 0;
        }
        return "Requires " + formatPlaytime(need) + " playtime (you have " + formatPlaytime(playMinutes)
                + ") — play " + formatPlaytime(remaining) + " more to unlock";
    }

    // Format minutes as "2h" or "1h 30m" or "45 min".
    private static String formatPlaytime(long minutes) {
        if (minutes >= 60 && minutes % 60 == 0) {
            return (minutes / 60) + "h";
        }
        if (minutes >= 60) {
            long h = minutes / 60;
            long m = minutes % 60;
            return h + "h " + m + "m";
        }
        return minutes + " min";
    }

    private enum SkillStatus { AVAILABLE, LOCKED, LEARNED }

    private JPanel createSkillPanel(LearnableSkill skill, SkillStatus status, String statusLine) {
        JPanel panel = AppTheme.createItemCard();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        String title = skill.getName();
        if (status == SkillStatus.LOCKED) {
            title += " 🔒";
        } else if (status == SkillStatus.LEARNED) {
            title += " ✓";
        }
        JLabel nameLabel = AppTheme.createCardTitle(title);
        infoPanel.add(nameLabel);

        JLabel descriptionLabel = AppTheme.createCardDescription(skill.getDescription());
        infoPanel.add(descriptionLabel);

        if (statusLine != null) {
            JLabel statusLabel = new JLabel("<html>" + statusLine + "</html>");
            statusLabel.setFont(AppTheme.FONT_SMALL);
            statusLabel.setForeground(status == SkillStatus.LEARNED
                    ? AppTheme.SUCCESS_COLOR.darker() : AppTheme.ERROR_COLOR);
            infoPanel.add(statusLabel);
        }

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        String costText = "Cost: $" + skill.getCost() + " | Needs: " + formatPlaytime(skill.getRequiredPlayTimeMinutes());
        JLabel costLabel = new JLabel(costText);
        costLabel.setFont(AppTheme.FONT_BUTTON);
        costLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(costLabel, BorderLayout.CENTER);

        JButton learnButton;
        if (status == SkillStatus.AVAILABLE) {
            learnButton = AppTheme.createSuccessButton("Learn ($" + skill.getCost() + ")");
            final LearnableSkill mySkill = skill;
            final JButton myButton = learnButton;
            learnButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    handleLearnSkill(mySkill, myButton);
                }
            });
        } else if (status == SkillStatus.LEARNED) {
            learnButton = AppTheme.createSuccessButton("Learned");
            learnButton.setEnabled(false);
        } else {
            learnButton = AppTheme.createSuccessButton("Locked");
            learnButton.setEnabled(false);
            learnButton.setToolTipText(statusLine);
        }
        actionPanel.add(learnButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Stays open after success; learned skills move to the Learned section.
    private void handleLearnSkill(LearnableSkill skill, JButton source) {
        GameController.ActionResult result = controller.learnSkill(skill.getName());
        gamePanel.addChatMessage(result.message);
        gamePanel.updateUserInfo();
        if (!result.success) {
            JOptionPane.showMessageDialog(this, result.message, "Cannot Learn", JOptionPane.WARNING_MESSAGE);
        } else {
            source.setEnabled(false);
            source.setText("Learned ✓");
            populateSkills();
        }
    }
}

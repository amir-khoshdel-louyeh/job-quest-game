package view;

import controller.GameController;
import model.LearnableSkill;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A dialog window that displays ONE profession's school lessons: available
 * ones can be learned, locked ones are visible but disabled with the unlock
 * requirement shown. Mirrors the workshop {@link JobDialog} pattern
 * (available first, then locked). The list is pre-filtered by identity, so a
 * Doctor never sees Programmer lessons.
 */
public class SkillDialog extends JDialog {

    private final GameController controller;
    private final GamePanel gamePanel;
    private final model.User user;
    private final List<LearnableSkill> allSkills;
    private final JPanel skillsContainer;

    // Dialog that lists the user's own profession school lessons
    public SkillDialog(JFrame parent, GameController controller, GamePanel gamePanel,
                       List<LearnableSkill> allSkills, model.User user) {
        super(parent, "School of Skills", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        this.allSkills = allSkills != null ? new ArrayList<>(allSkills) : new ArrayList<>();
        this.user = user;
        String userIdentity = user.getIdentity().getClass().getSimpleName();
        setTitle("School of Skills — " + userIdentity + " Lessons");
        setLayout(new BorderLayout(10, 10));
        // Same shared dialog look as the workshop jobs dialog
        AppTheme.styleDialog(this, 500, 600);
        // Close this dialog when the user closes the window
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

    // Split catalog into available / locked / learned and render sections.
    // Available first, locked after, learned last — one pass, no second list.
    private void populateSkills() {
        skillsContainer.removeAll();

        long playMinutes = user.getTotalPlayTime() / (1000 * 60);
        String userIdentity = user.getIdentity().getClass().getSimpleName();
        List<String> learnedNames = user.getSkills().stream()
                .map(model.Skill::getName).toList();

        List<LearnableSkill> available = new ArrayList<>();
        List<LearnableSkill> locked = new ArrayList<>();
        List<LearnableSkill> learned = new ArrayList<>();
        for (LearnableSkill skill : allSkills) {
            // Defensive: drop other professions' lessons if an unfiltered list arrives.
            if (!skill.isForIdentity(userIdentity)) {
                continue;
            }
            if (learnedNames.contains(skill.getName())) {
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

    /** Human-readable reason a skill is locked (playtime gate + how to open). */
    private static String lockReason(LearnableSkill skill, long playMinutes) {
        long need = skill.getRequiredPlayTimeMinutes();
        long remaining = Math.max(0, need - playMinutes);
        return "🔒 Requires " + formatPlaytime(need) + " playtime (you have " + formatPlaytime(playMinutes)
                + ") — play " + formatPlaytime(remaining) + " more to unlock";
    }

    private static String formatPlaytime(long minutes) {
        if (minutes >= 60 && minutes % 60 == 0) {
            return (minutes / 60) + "h";
        }
        if (minutes >= 60) {
            return String.format("%dh %dm", minutes / 60, minutes % 60);
        }
        return minutes + " min";
    }

    private enum SkillStatus { AVAILABLE, LOCKED, LEARNED }

    // Create a single skill entry panel with action button
    private JPanel createSkillPanel(LearnableSkill skill, SkillStatus status, String statusLine) {
        // One card factory — same as the workshop jobs dialog
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

        String costText = String.format("Cost: $%d | Needs: %s",
                skill.getCost(), formatPlaytime(skill.getRequiredPlayTimeMinutes()));
        JLabel costLabel = new JLabel(costText);
        costLabel.setFont(AppTheme.FONT_BUTTON);
        costLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(costLabel, BorderLayout.CENTER);

        JButton learnButton;
        switch (status) {
            case AVAILABLE -> {
                learnButton = AppTheme.createSuccessButton(String.format("Learn ($%d)", skill.getCost()));
                // Delegate the learning action to a small helper to keep this UI method short.
                learnButton.addActionListener(evt -> handleLearnSkill(skill, learnButton));
            }
            case LEARNED -> {
                learnButton = AppTheme.createSuccessButton("Learned ✓");
                learnButton.setEnabled(false);
            }
            default -> {
                learnButton = AppTheme.createSuccessButton("Locked 🔒");
                learnButton.setEnabled(false);
                learnButton.setToolTipText(statusLine);
            }
        }
        actionPanel.add(learnButton, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Helper method to perform learning via controller and show results simply.
    // Stays open after success (like the workshop dialog); the learned skill
    // moves to the "Learned" section via populateSkills() refresh.
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

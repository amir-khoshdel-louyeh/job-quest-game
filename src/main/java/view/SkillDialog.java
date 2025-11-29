package view;

import controller.GameCommands;
import controller.Outcome;
import model.career.LearnableSkill;
import model.career.SkillAvailability;
import model.player.PlayerView;
import utils.DurationText;
import utils.Lists;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// The school screen: learn your career's lessons, locked ones show playtime needed.
public class SkillDialog extends JDialog {

    private final GameCommands controller;
    private final GamePanel gamePanel;
    private final PlayerView player;
    private final List<LearnableSkill> allSkills;
    private final JPanel skillsContainer;

    public SkillDialog(JFrame parent, GameCommands controller, GamePanel gamePanel,
                       List<LearnableSkill> allSkills, PlayerView player) {
        super(parent, "School of Skills", true);
        this.controller = controller;
        this.gamePanel = gamePanel;
        this.allSkills = Lists.copyOf(allSkills);
        this.player = player;
        String userIdentity = player.getIdentityName();
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

        long playMinutes = player.getPlayTimeMinutes();
        List<LearnableSkill> available = new ArrayList<>();
        List<LearnableSkill> locked = new ArrayList<>();
        List<LearnableSkill> learned = new ArrayList<>();
        for (LearnableSkill skill : allSkills) {
            SkillAvailability availability = skill.availabilityFor(
                    player.getSkillSet(), playMinutes);
            if (availability == SkillAvailability.LEARNED) {
                learned.add(skill);
            } else if (availability == SkillAvailability.AVAILABLE) {
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
            skillsContainer.add(createSkillPanel(skill, SkillAvailability.AVAILABLE, null));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        if (!available.isEmpty() && !locked.isEmpty()) {
            skillsContainer.add(sectionHeader("🔒 Locked"));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        for (LearnableSkill skill : locked) {
            skillsContainer.add(createSkillPanel(skill, SkillAvailability.LOCKED,
                    lockReason(skill, playMinutes)));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        if (!learned.isEmpty()) {
            skillsContainer.add(sectionHeader("✓ Learned"));
            skillsContainer.add(Box.createVerticalStrut(10));
        }
        for (LearnableSkill skill : learned) {
            skillsContainer.add(createSkillPanel(skill, SkillAvailability.LEARNED,
                    "✓ Already learned"));
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
        long remaining = skill.minutesUntilUnlocked(playMinutes);
        return "Requires " + DurationText.formatMinutes(need)
                + " playtime (you have " + DurationText.formatMinutes(playMinutes)
                + ") — play " + DurationText.formatMinutes(remaining) + " more to unlock";
    }

    private JPanel createSkillPanel(LearnableSkill skill, SkillAvailability status,
                                    String statusLine) {
        JPanel panel = AppTheme.createItemCard();

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        String title = skill.getName();
        if (status == SkillAvailability.LOCKED) {
            title += " 🔒";
        } else if (status == SkillAvailability.LEARNED) {
            title += " ✓";
        }
        JLabel nameLabel = AppTheme.createCardTitle(title);
        infoPanel.add(nameLabel);

        JLabel descriptionLabel = AppTheme.createCardDescription(skill.getDescription());
        infoPanel.add(descriptionLabel);

        if (statusLine != null) {
            JLabel statusLabel = new JLabel("<html>" + statusLine + "</html>");
            statusLabel.setFont(AppTheme.FONT_SMALL);
            statusLabel.setForeground(status == SkillAvailability.LEARNED
                    ? AppTheme.SUCCESS_COLOR.darker() : AppTheme.ERROR_COLOR);
            infoPanel.add(statusLabel);
        }

        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);

        String costText = "Cost: $" + skill.getCost() + " | Needs: "
                + DurationText.formatMinutes(skill.getRequiredPlayTimeMinutes());
        JLabel costLabel = new JLabel(costText);
        costLabel.setFont(AppTheme.FONT_BUTTON);
        costLabel.setForeground(AppTheme.MONEY_COLOR.darker());
        actionPanel.add(costLabel, BorderLayout.CENTER);

        JButton learnButton;
        if (status == SkillAvailability.AVAILABLE) {
            learnButton = AppTheme.createSuccessButton("Learn ($" + skill.getCost() + ")");
            learnButton.addActionListener(e -> handleLearnSkill(skill, learnButton));
        } else if (status == SkillAvailability.LEARNED) {
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
        Outcome result = controller.learnSkill(skill.getName());
        gamePanel.addChatMessage(result.getMessage());
        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Cannot Learn", JOptionPane.WARNING_MESSAGE);
        } else {
            source.setEnabled(false);
            source.setText("Learned ✓");
            populateSkills();
        }
    }
}

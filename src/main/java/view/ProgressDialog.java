package view;

import controller.GameController;
import model.Achievement;
import model.Challenge;
import services.AchievementService;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// The progress screen: challenges, achievements, and stats in tabs.
public class ProgressDialog extends JDialog {

    private final GameController controller;

    public ProgressDialog(JFrame parent, GameController controller, GamePanel gamePanel) {
        super(parent, "📊 Progress", true);
        this.controller = controller;
        AppTheme.styleDialog(this, 560, 620);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(AppTheme.FONT_BUTTON);
        tabs.addTab("🏆 Challenges", buildChallengesTab());
        tabs.addTab("🏅 Achievements", buildAchievementsTab());
        tabs.addTab("📈 Stats", buildStatsTab());
        add(tabs, BorderLayout.CENTER);

        JButton close = AppTheme.createSecondaryButton("Close");
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(AppTheme.BACKGROUND_COLOR);
        footer.add(close);
        add(footer, BorderLayout.SOUTH);
    }

    private JComponent buildChallengesTab() {
        List<Challenge> active = controller.getActiveChallenges();
        List<Challenge> done = controller.getCompletedChallenges();

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(AppTheme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel h1 = AppTheme.createCardTitle("Active (" + active.size() + ")");
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(h1);
        root.add(Box.createVerticalStrut(6));
        if (active.isEmpty()) {
            root.add(muted("All challenges complete — nice!"));
        } else {
            for (Challenge c : active) {
                root.add(challengeRow(c, false));
                root.add(Box.createVerticalStrut(8));
            }
        }
        root.add(Box.createVerticalStrut(12));
        JLabel h2 = AppTheme.createCardTitle("Completed (" + done.size() + ")");
        h2.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(h2);
        root.add(Box.createVerticalStrut(6));
        if (done.isEmpty()) {
            root.add(muted("None yet — do jobs to complete your first challenge."));
        } else {
            for (Challenge c : done) {
                root.add(challengeRow(c, true));
                root.add(Box.createVerticalStrut(8));
            }
        }
        return scroll(root);
    }

    private JPanel challengeRow(Challenge c, boolean completed) {
        JPanel card = AppTheme.createItemCard();
        card.setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel name = AppTheme.createCardTitle((completed ? "✅ " : "🎯 ") + c.getName());
        top.add(name, BorderLayout.WEST);
        JLabel reward = new JLabel("+$" + c.getRewardMoney() + " +" + c.getRewardExperience() + " XP");
        reward.setFont(AppTheme.FONT_SMALL);
        reward.setForeground(AppTheme.MONEY_COLOR.darker());
        top.add(reward, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        JLabel desc = AppTheme.createCardDescription(
                c.getDescription() + "<br>" + c.getCurrentProgress() + "/" + c.getTargetValue());
        card.add(desc, BorderLayout.CENTER);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(completed ? 100 : c.getProgressPercentage());
        bar.setStringPainted(true);
        bar.setString(completed ? "Done" : c.getProgressPercentage() + "%");
        bar.setForeground(completed ? AppTheme.SUCCESS_COLOR : AppTheme.PRIMARY_COLOR);
        bar.setBackground(AppTheme.DIVIDER_COLOR);
        bar.setFont(AppTheme.FONT_SMALL);
        card.add(bar, BorderLayout.SOUTH);
        return card;
    }

    private JComponent buildAchievementsTab() {
        List<Achievement> all = controller.getAchievementsWithStatus();
        int unlocked = 0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).isUnlocked()) {
                unlocked = unlocked + 1;
            }
        }

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(AppTheme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel h = AppTheme.createCardTitle("Unlocked " + unlocked + "/" + all.size());
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(h);
        root.add(Box.createVerticalStrut(6));
        for (Achievement a : all) {
            root.add(achievementRow(a));
            root.add(Box.createVerticalStrut(8));
        }
        return scroll(root);
    }

    private JPanel achievementRow(Achievement a) {
        JPanel card = AppTheme.createItemCard();
        card.setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel name = AppTheme.createCardTitle((a.isUnlocked() ? "✅ " : "🔒 ") + a.getName());
        if (!a.isUnlocked()) {
            name.setForeground(AppTheme.TEXT_SECONDARY);
        }
        top.add(name, BorderLayout.WEST);
        JLabel reward = new JLabel("+$" + a.getRewardMoney());
        reward.setFont(AppTheme.FONT_SMALL);
        reward.setForeground(AppTheme.MONEY_COLOR.darker());
        top.add(reward, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        card.add(AppTheme.createCardDescription(a.getDescription()), BorderLayout.CENTER);

        // Get progress, clamped to 0-100.
        int pct = 0;
        try {
            int p = AchievementService.getInstance().getAchievementProgress(
                    controller.getUserController().getUser(), a);
            if (p < 0) {
                p = 0;
            }
            if (p > 100) {
                p = 100;
            }
            pct = p;
        } catch (Exception e) {
            if (a.isUnlocked()) {
                pct = 100;
            } else {
                pct = 0;
            }
        }
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(a.isUnlocked() ? 100 : pct);
        bar.setStringPainted(true);
        bar.setString(a.isUnlocked() ? "Unlocked" : pct + "%");
        bar.setForeground(a.isUnlocked() ? AppTheme.SUCCESS_COLOR : AppTheme.INFO_COLOR);
        bar.setBackground(AppTheme.DIVIDER_COLOR);
        bar.setFont(AppTheme.FONT_SMALL);
        card.add(bar, BorderLayout.SOUTH);
        return card;
    }

    private JComponent buildStatsTab() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(AppTheme.BACKGROUND_COLOR);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel session = new JLabel(controller.getSessionStats());
        session.setFont(AppTheme.FONT_BODY);
        session.setForeground(AppTheme.TEXT_SECONDARY);
        session.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        root.add(session, BorderLayout.NORTH);

        JTextArea stats = new JTextArea(controller.getDetailedStats());
        stats.setEditable(false);
        stats.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        stats.setBackground(AppTheme.SURFACE_COLOR);
        stats.setForeground(AppTheme.TEXT_PRIMARY);
        stats.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        root.add(new JScrollPane(stats), BorderLayout.CENTER);
        return root;
    }

    private static JScrollPane scroll(JPanel content) {
        JScrollPane sp = new JScrollPane(content);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.getViewport().setBackground(AppTheme.BACKGROUND_COLOR);
        return sp;
    }

    private static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        l.setForeground(AppTheme.TEXT_SECONDARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}

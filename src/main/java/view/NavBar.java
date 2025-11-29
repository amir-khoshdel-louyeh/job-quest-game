package view;

import controller.DeveloperController;
import controller.GameController;
import controller.GameQueries;
import model.career.Job;
import model.career.LearnableSkill;
import model.player.PlayerView;
import view.minigame.QuizLauncher;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Top bar + the screens it opens (hospital, workshop, market,
// school, progress, dev controls). Owned by GamePanel.
public class NavBar extends JPanel implements WorldViewPanel.Navigator {

    private final GameController controller;
    private final GameQueries queries;
    private final DeveloperController developerController;
    private final PlayerView player;
    private final GamePanel host;
    private final view.minigame.MiniGameLauncher workGame = new QuizLauncher();

    public NavBar(GameController controller, GameQueries queries,
                  DeveloperController developerController,
                  PlayerView player, GamePanel host) {
        super(new FlowLayout(FlowLayout.CENTER, 8, 4));
        if (controller == null) {
            throw new IllegalArgumentException("controller must not be null");
        }
        if (queries == null) {
            throw new IllegalArgumentException("queries must not be null");
        }
        if (developerController == null) {
            throw new IllegalArgumentException("developerController must not be null");
        }
        if (player == null) {
            throw new IllegalArgumentException("player must not be null");
        }
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }
        this.controller = controller;
        this.queries = queries;
        this.developerController = developerController;
        this.player = player;
        this.host = host;
        setBackground(AppTheme.BACKGROUND_COLOR);
        add(navButton("Hospital", "Buy health services (heal)", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openHospital();
            }
        }));
        add(navButton("Workshop", "Do jobs to earn money and XP", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openWorkshop();
            }
        }));
        add(navButton("Market", "Buy energy services (eat)", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openMarket();
            }
        }));
        add(navButton("School", "Learn skills to unlock better jobs", true, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSchool();
            }
        }));
        add(navButton("Progress", "Challenges, achievements and stats", false, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openProgress();
            }
        }));
        add(navButton("Dev", "Developer demo controls (change variables)", false, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openDevControls();
            }
        }));
    }

    @Override
    public void openHospital() {
        new ServiceDialog(frame(), controller, host, queries.getHealthServices()).setVisible(true);
    }

    @Override
    public void openWorkshop() {
        List<Job> jobs = queries.getJobsForCurrentIdentity();
        if (jobs.isEmpty()) {
            host.addChatMessage("No jobs are available for your current identity/skills.");
            return;
        }
        new JobDialog(frame(), controller, host, jobs, player, workGame).setVisible(true);
    }

    @Override
    public void openMarket() {
        new ServiceDialog(frame(), controller, host, queries.getEnergyServices()).setVisible(true);
    }

    @Override
    public void openSchool() {
        java.util.List<LearnableSkill> mySkills = queries.getSkillsForCurrentIdentity();
        new SkillDialog(frame(), controller, host, mySkills, player).setVisible(true);
    }

    public void openProgress() {
        new ProgressDialog(frame(), queries, host).setVisible(true);
    }

    public void openDevControls() {
        new DeveloperDialog(frame(), developerController, host).setVisible(true);
    }

    private JFrame frame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    private JButton navButton(String text, String tooltip, boolean primary,
                              java.awt.event.ActionListener action) {
        JButton b = primary ? AppTheme.createPrimaryButton(text) : AppTheme.createSecondaryButton(text);
        b.setToolTipText(tooltip);
        b.addActionListener(action);
        return b;
    }
}

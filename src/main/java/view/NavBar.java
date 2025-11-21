package view;

import controller.DeveloperController;
import controller.GameController;
import controller.UserController;
import model.career.Job;
import model.career.LearnableSkill;
import provider.ServiceProvider;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;

// Top navigation bar + the screens it opens (hospital, workshop, market,
// school, progress, dev controls). Extracted from GamePanel (SRP):
// GamePanel keeps layout/timers, this owns "where to go".
public class NavBar extends JPanel {

    private final GameController controller;
    private final UserController userController;
    private final GamePanel host;

    public NavBar(GameController controller, UserController userController, GamePanel host) {
        super(new FlowLayout(FlowLayout.CENTER, 8, 4));
        if (controller == null) {
            throw new IllegalArgumentException("controller must not be null");
        }
        if (userController == null) {
            throw new IllegalArgumentException("userController must not be null");
        }
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }
        this.controller = controller;
        this.userController = userController;
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

    public void openHospital() {
        new ServiceDialog(frame(), controller, host, ServiceProvider.getHealthServices()).setVisible(true);
    }

    public void openWorkshop() {
        GameController.ActionResult result = controller.doWork();
        if (result.isSuccess() && result.getNextAction() == GameController.NextAction.OPEN_JOB_DIALOG) {
            java.util.List<Job> myJobs = controller.getJobsForCurrentIdentity();
            new JobDialog(frame(), controller, host, myJobs, userController).setVisible(true);
        } else {
            host.addChatMessage(result.getMessage());
        }
    }

    public void openMarket() {
        new ServiceDialog(frame(), controller, host, ServiceProvider.getEnergyServices()).setVisible(true);
    }

    public void openSchool() {
        java.util.List<LearnableSkill> mySkills = controller.getSkillsForCurrentIdentity();
        new SkillDialog(frame(), controller, host, mySkills, userController).setVisible(true);
    }

    public void openProgress() {
        new ProgressDialog(frame(), controller, host).setVisible(true);
    }

    public void openDevControls() {
        DeveloperController dev = new DeveloperController(userController);
        new DeveloperDialog(frame(), dev, host).setVisible(true);
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

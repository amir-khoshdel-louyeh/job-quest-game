package view;

import controller.contract.GameCommands;
import controller.contract.GameReads;
import model.career.Job;
import model.career.Skill;
import model.player.PlayerView;
import view.contract.MiniGameLauncher;
import view.contract.Navigator;
import view.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Top bar + the screens it opens (hospital, workshop, market,
// school, progress). Owned by GamePanel.
public class NavBarPanel extends JPanel implements Navigator {

    private final GameCommands controller;
    private final GameReads queries;
    private final PlayerView player;
    private final GamePanel host;
    private final MiniGameLauncher workGame;

    NavBarPanel(GameCommands controller, GameReads queries,
                  PlayerView player, GamePanel host, MiniGameLauncher workGame) {
        super(new FlowLayout(FlowLayout.CENTER, 8, 4));
        if (controller == null) {
            throw new IllegalArgumentException("controller must not be null");
        }
        if (queries == null) {
            throw new IllegalArgumentException("queries must not be null");
        }
        if (player == null) {
            throw new IllegalArgumentException("player must not be null");
        }
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }
        if (workGame == null) {
            throw new IllegalArgumentException("workGame must not be null");
        }
        this.controller = controller;
        this.queries = queries;
        this.player = player;
        this.host = host;
        this.workGame = workGame;
        setBackground(AppTheme.BACKGROUND_COLOR);
        add(navButton("Hospital", "Buy health services (heal)", true, e -> openHospital()));
        add(navButton("Workshop", "Do jobs to earn money and XP", true, e -> openWorkshop()));
        add(navButton("Market", "Buy energy services (eat)", true, e -> openMarket()));
        add(navButton("School", "Learn skills to unlock better jobs", true, e -> openSchool()));
        add(navButton("Progress", "Challenges, achievements and stats", false, e -> openProgress()));
    }

    @Override
    public void openHospital() {
        new ShopDialog(frame(), controller, host, queries.getHealthItems()).setVisible(true);
    }

    @Override
    public void openWorkshop() {
        List<Job> jobs = queries.getJobsForCurrentIdentity();
        if (jobs.isEmpty()) {
            host.addChatMessage("No jobs are available for your current profession/skills.");
            return;
        }
        new JobDialog(frame(), controller, host, jobs, player, workGame).setVisible(true);
    }

    @Override
    public void openMarket() {
        new ShopDialog(frame(), controller, host, queries.getEnergyItems()).setVisible(true);
    }

    @Override
    public void openSchool() {
        java.util.List<Skill> mySkills = queries.getSkillsForCurrentIdentity();
        new SkillDialog(frame(), controller, host, mySkills, player).setVisible(true);
    }

    public void openProgress() {
        new ProgressDialog(frame(), queries).setVisible(true);
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

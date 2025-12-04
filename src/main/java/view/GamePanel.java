package view;

// The main game screen: stats on the side, world in the middle, chat at the bottom.
import controller.contract.GameCommands;
import controller.contract.GameContext;
import controller.contract.MessageSink;
import controller.contract.RoutineControl;
import model.player.PlayerView;
import observer.ChangeType;
import observer.Observable;
import observer.Observer;
import view.contract.MiniGameLauncher;
import view.theme.AppTheme;
import utils.GameLog;
import utils.Require;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class GamePanel extends JPanel implements Observer, MessageSink {
    private final GameCommands controller;
    private final RoutineControl routineController;
    private final Observable observable;
    private final PlayerView player;
    private final PlayerStatsPanel statsPanel;
    private final ChatPanel chatPanel;
    private final NavBarPanel navBar;
    // Dispatch table instead of a switch: each change type knows which
    // section to repaint. SKILLS has no entry (nothing skill-related shows
    // here), so update() falls back to a full refresh for it.
    private final Map<ChangeType, Consumer<PlayerStatsPanel>> refreshers = new EnumMap<>(ChangeType.class);
    {
        refreshers.put(ChangeType.BALANCE, PlayerStatsPanel::refreshBalance);
        refreshers.put(ChangeType.HEALTH, PlayerStatsPanel::refreshHealth);
        refreshers.put(ChangeType.ENERGY, PlayerStatsPanel::refreshEnergy);
        refreshers.put(ChangeType.XP, PlayerStatsPanel::refreshProgress);
        refreshers.put(ChangeType.REPUTATION, PlayerStatsPanel::refreshProgress);
        refreshers.put(ChangeType.JOBS, PlayerStatsPanel::refreshProgress);
    }
    private final JPanel rightPanel;
    private final SessionPanel sessionPanel;
    private final JButton bottomExitButton;
    GamePanel(GameContext context, MiniGameLauncher workGame) {
        this.controller = context.gameController();
        this.player = context.playerView();
        this.observable = context.observable();
        this.observable.addObserver(this); // Get stat updates.
        Require.notNull(workGame, "workGame");
        this.routineController = context.createRoutineController(this, new SwingScheduler());
        setLayout(new BorderLayout(10,10));
        setBackground(AppTheme.BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Job-Quest-Game", SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        this.rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(240, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        rightPanel.setBackground(AppTheme.BACKGROUND_COLOR);

        this.statsPanel = new PlayerStatsPanel(player);

        this.sessionPanel = new SessionPanel();

        this.bottomExitButton = AppTheme.createDangerButton("❌ Exit");
        bottomExitButton.setAction(sessionPanel.getExitAction());

        rightPanel.add(statsPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(sessionPanel);

        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setBorder(null); // The inner panel already has borders
        add(rightScrollPane, BorderLayout.EAST);

        JPanel bottomActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomActionsPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        bottomActionsPanel.add(bottomExitButton);

        this.navBar = new NavBarPanel(controller, context.gameQueries(),
                player, this, workGame);
        WorldViewPanel worldView = new WorldViewPanel(navBar);
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setBackground(AppTheme.BACKGROUND_COLOR);
        centerPanel.add(navBar, BorderLayout.NORTH);
        centerPanel.add(worldView, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        this.chatPanel = new ChatPanel();

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(chatPanel, BorderLayout.CENTER);
        southPanel.add(bottomActionsPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

    // Start periodic background tasks.
    routineController.startSchedulers();
    // Show onboarding hints for fresh accounts.
    maybeShowOnboarding();

        if (player.isBlocked()) {
            JOptionPane.showMessageDialog(this,
                "Your account is blocked until " + new java.util.Date(player.getBlockedUntil()));
        }
    }

    // Selective refresh via the dispatch table above — no switch.
    @Override
    public void update(ChangeType whatChanged) {
        if (whatChanged == null) {
            refreshAll();
            return;
        }
        Consumer<PlayerStatsPanel> refresh = refreshers.get(whatChanged);
        if (refresh == null) {
            refreshAll();
            return;
        }
        refresh.accept(statsPanel);
    }

    private void refreshAll() {
        statsPanel.refresh();
    }

    // Lifecycle: MainFrame swaps content panes without disposing the old one,
    // so detach here — stop timers + unsubscribe — or dead panels keep
    // receiving updates and draining stats in the background (observer leak).
    // Swing calls removeNotify when this panel leaves the component hierarchy.
    @Override
    public void removeNotify() {
        try {
            disposePanel();
        } catch (RuntimeException e) {
            // Detach is best-effort: the panel is already leaving the hierarchy,
            // so log the failure and still let Swing finish its own cleanup.
            GameLog.error("disposing game panel", e);
        }
        super.removeNotify();
    }

    // Explicit detach (also called from removeNotify above).
    public void disposePanel() {
        try {
            observable.removeObserver(this);
        } catch (RuntimeException e) {
            // No checked failures here; keep going so the scheduler still stops.
            GameLog.error("removing panel observer", e);
        }
        try {
            routineController.stopSchedulers();
        } catch (RuntimeException e) {
            // Same rule: cleanup of the other collaborators must not be skipped.
            GameLog.error("stopping schedulers", e);
        }
    }

    private void maybeShowOnboarding() {
        boolean fresh = player.getTotalJobsCompleted() == 0
                && player.getSkillCount() == 0;
        if (!fresh) return;
        addChatMessage("👋 Welcome to Job Quest! Your goal: earn, learn skills, unlock better jobs.");
        addChatMessage("🎯 Next: 1) 🔨 Workshop — do your first job  2) 📊 Progress — track challenges  3) 🎓 School — learn a skill");
        addChatMessage("💡 Tip: low bars? 🛒 Market restores energy, 🏥 Hospital restores health. F11 toggles windowed mode.");
    }
    

    // Delegate: chat lives in ChatPanel; this stays a MessageSink
    // so existing callers (dialogs, RoutineController) don't change.
    @Override
    public void addChatMessage(String msg) {
        chatPanel.addChatMessage(msg);
    }

}

package controller;

import model.Identity;
import model.player.PlayerView;
import model.player.User;
import observer.Observable;
import provider.AchievementProvider;
import provider.ChallengeProvider;
import provider.JobProvider;
import provider.ServiceProvider;
import provider.SkillProvider;
import services.AchievementService;
import services.ChallengeService;
import services.JobService;
import services.RoutineService;
import services.job.JobGate;
import utils.Require;

// One game run: creates the player aggregate, its shared services, its
// controllers, and its read queries. This is the composition root, so it is
// the only class that names concrete implementations; everything it hands
// out is an abstraction.
public final class GameSession implements GameContext {
    private final UserController userController;
    private final PlayerView playerView;
    private final GameCommands gameController;
    private final GameReads gameQueries;
    private final DeveloperCommands developerController;

    // ----- Construction -----
    private GameSession(UserController userController, PlayerView playerView,
                        GameCommands gameController, GameReads gameQueries,
                        DeveloperCommands developerController) {
        this.userController = userController;
        this.playerView = playerView;
        this.gameController = gameController;
        this.gameQueries = gameQueries;
        this.developerController = developerController;
    }

    // Starts a run at the current time.
    public static GameSession start(String username, Identity identity) {
        Require.notNull(identity, "identity");
        User user = new User(username, identity, identity.getStartingBalance());
        user.grantStartingBonus();
        UserController users = new UserController(user);
        // Views get projections, not the adapter: they can read the player and
        // subscribe to changes, but cannot cast what they hold to a command
        // port, to Subject, or to the mutable User aggregate.
        PlayerView playerView = users.playerView();

        // One catalog/service instance per run: job, skill, challenge, and
        // achievement rules must see the same state. Catalogs are passed in
        // explicitly so no rule class hides a second copy of its own.
        ChallengeProvider challengeCatalog = new ChallengeProvider();
        AchievementProvider achievementCatalog = new AchievementProvider();
        JobProvider jobs = new JobProvider();
        SkillProvider skills = new SkillProvider();
        ServiceProvider services = new ServiceProvider();

        ChallengeService challenges = new ChallengeService(challengeCatalog);
        AchievementService achievements = new AchievementService(achievementCatalog);
        EconomyController economy = new EconomyController(users, services);
        JobController jobController = new JobController(
                users, new JobService(), new JobGate(), challenges, achievements);
        SkillController skillController = new SkillController(
                users, challenges, achievements, skills);
        SessionTracker session = new SessionTracker(System.currentTimeMillis());
        GameController game = new GameController(economy, jobController, skillController, session);
        GameQueries queries = new GameQueries(users, services, jobs, skills, playerView,
                challenges, achievements, session);

        // A fresh run starts with the story challenges available.
        challenges.initializeChallenges(users);
        return new GameSession(users, playerView, game, queries,
                new DeveloperController(playerView, users.devControls(), users));
    }

    // ----- Getter -----
    @Override
    public PlayerView playerView() {
        return playerView;
    }

    // Views may subscribe, but cannot notify or mutate.
    @Override
    public Observable observable() {
        return userController.observable();
    }

    @Override
    public DeveloperCommands developerController() {
        return developerController;
    }

    @Override
    public GameCommands gameController() {
        return gameController;
    }

    // Read-only screen data; no player mutations are reachable from here.
    @Override
    public GameReads gameQueries() {
        return gameQueries;
    }

    // Builds the upkeep controller with the caller's scheduler and message sink.
    @Override
    public RoutineControl createRoutineController(MessageSink messages, Scheduler scheduler) {
        return new RoutineController(userController, new RoutineService(), messages, scheduler);
    }
}

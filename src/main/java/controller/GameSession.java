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
import services.job.JobGate;

// One game run: creates the player aggregate, its shared services, its
// controllers, and its read queries. This is the composition root, so views
// receive a read-only PlayerView, a read-only Observable, and GameQueries
// instead of the mutable controllers.
public final class GameSession {
    private final UserController userController;
    private final PlayerView playerView;
    private final GameController gameController;
    private final GameQueries gameQueries;
    private final DeveloperController developerController;

    // ----- Construction -----
    private GameSession(UserController userController, PlayerView playerView,
                        GameController gameController, GameQueries gameQueries,
                        DeveloperController developerController) {
        this.userController = userController;
        this.playerView = playerView;
        this.gameController = gameController;
        this.gameQueries = gameQueries;
        this.developerController = developerController;
    }

    // Starts a run at the current time.
    public static GameSession start(String username, Identity identity) {
        return start(username, identity, System.currentTimeMillis());
    }

    // Starts a run with a fixed clock for tests.
    public static GameSession start(String username, Identity identity, long startTime) {
        utils.Require.notNull(identity, "identity");
        User user = new User(username, identity, identity.getStartingBalance());
        user.grantStartingBonus();
        UserController users = new UserController(user);
        PlayerView playerView = new PlayerReadModel(user);

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
        SessionTracker session = new SessionTracker(startTime);
        GameController game = new GameController(economy, jobController, skillController, session);
        GameQueries queries = new GameQueries(users, economy, jobs, skills, playerView,
                new StatsController(), challenges, achievements, session);

        // A fresh run starts with the story challenges available.
        challenges.initializeChallenges(users);
        return new GameSession(users, playerView, game, queries, new DeveloperController(users));
    }

    // ----- Getter -----
    public PlayerView playerView() {
        return playerView;
    }

    // Views may subscribe, but cannot notify or mutate.
    public Observable observable() {
        return userController;
    }

    public DeveloperController developerController() {
        return developerController;
    }

    public GameController gameController() {
        return gameController;
    }

    // Read-only screen data; no player mutations are reachable from here.
    public GameQueries gameQueries() {
        return gameQueries;
    }

    // Builds the upkeep controller with the caller's scheduler and message sink.
    public RoutineController createRoutineController(MessageSink messages, Scheduler scheduler) {
        return new RoutineController(userController, messages, scheduler);
    }
}

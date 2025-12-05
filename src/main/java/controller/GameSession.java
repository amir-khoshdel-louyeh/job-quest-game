package controller;

import controller.contract.GameCommands;
import controller.contract.GameContext;
import controller.contract.GameReads;
import controller.contract.MessageSink;
import controller.contract.RoutineControl;
import controller.contract.Scheduler;
import model.career.Profession;
import model.player.PlayerView;
import model.player.Player;
import observer.Observable;
import provider.AchievementProvider;
import provider.ChallengeProvider;
import provider.JobProvider;
import provider.ProfessionRegistry;
import provider.ShopItemProvider;
import provider.SkillProvider;
import services.AchievementService;
import services.ChallengeService;
import services.JobRewardService;
import services.RoutineService;
import services.job.JobGate;
import utils.Require;

// One game run: creates the player aggregate, its shared services, its
// controllers, and its read queries. This is the composition root, so it is
// the only class that names concrete implementations; everything it hands
// out is an abstraction.
public final class GameSession implements GameContext {
    private final PlayerController playerController;
    private final PlayerView playerView;
    private final GameCommands gameController;
    private final GameReads gameQueries;

    // ----- Construction -----
    private GameSession(PlayerController playerController, PlayerView playerView,
                        GameCommands gameController, GameReads gameQueries) {
        this.playerController = playerController;
        this.playerView = playerView;
        this.gameController = gameController;
        this.gameQueries = gameQueries;
    }

    // Starts a run at the current time.
    public static GameSession start(String playerName, Profession profession) {
        Require.notNull(profession, "profession");
        Player player = new Player(playerName, profession, profession.getStartingBalance());
        player.grantStartingBonus();
        PlayerController playerController = new PlayerController(player);
        PlayerView playerView = playerController.playerView();

        // One catalog/service instance per run: job, skill, challenge, and
        ProfessionRegistry professions = new ProfessionRegistry();
        ChallengeProvider challengeCatalog = new ChallengeProvider();
        AchievementProvider achievementCatalog = new AchievementProvider();
        JobProvider jobs = new JobProvider(professions);
        SkillProvider skills = new SkillProvider(professions);
        ShopItemProvider services = new ShopItemProvider();

        ChallengeService challenges = new ChallengeService(challengeCatalog);
        AchievementService achievements = new AchievementService(achievementCatalog);
        EconomyController economy = new EconomyController(playerController, services);
        JobController jobController = new JobController(
                playerController, new JobRewardService(), new JobGate(), challenges, achievements);
        SkillController skillController = new SkillController(
                playerController, challenges, achievements, skills);
        SessionTracker session = new SessionTracker(System.currentTimeMillis());
        GameController game = new GameController(economy, jobController, skillController, session);
        GameQueries queries = new GameQueries(playerController, services, jobs, skills, playerView,
                challenges, achievements, session);

        // A fresh run starts with the story challenges available.
        challenges.initializeChallenges(playerController);
        return new GameSession(playerController, playerView, game, queries);
    }

    // ----- Getter -----
    @Override
    public PlayerView playerView() {
        return playerView;
    }

    @Override
    public Observable observable() {
        return playerController.observable();
    }

    @Override
    public GameCommands gameController() {
        return gameController;
    }

    @Override
    public GameReads gameQueries() {
        return gameQueries;
    }

    @Override
    public RoutineControl createRoutineController(MessageSink messages, Scheduler scheduler) {
        return new RoutineController(playerController, new RoutineService(), messages, scheduler);
    }
}

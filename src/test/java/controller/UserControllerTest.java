package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import model.career.Job;
import model.career.Profession;
import model.minigame.MiniGameResult;
import model.player.CareerProgress;
import model.player.JobRecord;
import model.player.PlayerView;
import model.player.PurchaseTarget;
import model.player.User;
import model.player.Vitals;
import model.store.Service;
import observer.ChangeType;
import observer.Observer;
import org.junit.jupiter.api.Test;
import provider.ServiceProvider;
import utils.DurationText;

// Controller rules: play time, level-up notifications, and purchase outcomes.
class UserControllerTest {

    private Profession programmer() {
        return new Profession("Programmer", "Programmer", "", 0);
    }

    private User user(int balance) {
        return new User("Tester", programmer(), balance);
    }

    private static final class RecordingObserver implements Observer {
        private final List<ChangeType> events = new ArrayList<ChangeType>();

        @Override
        public void update(ChangeType whatChanged) {
            events.add(whatChanged);
        }

        boolean saw(ChangeType type) {
            return events.contains(type);
        }
    }

    @Test
    void playTimeAccumulatesAndIgnoresNonPositiveValues() {
        UserController users = new UserController(user(0));

        users.addPlayTime(DurationText.MILLIS_PER_MINUTE * 2);
        users.addPlayTime(-1);

        assertEquals(2, users.getPlayTimeMinutes());
    }

    private static final Scheduler NO_SCHEDULER = (period, task) -> () -> { };

    // Captures scheduled tasks so timer wiring can be checked without Swing.
    private static final class FakeScheduler implements Scheduler {
        private final List<Long> periods = new ArrayList<Long>();
        private final List<Runnable> tasks = new ArrayList<Runnable>();
        private final List<Runnable> cancels = new ArrayList<Runnable>();

        @Override
        public Cancelable every(long periodMillis, Runnable task) {
            periods.add(periodMillis);
            tasks.add(task);
            return () -> cancels.add(task);
        }
    }

    @Test
    void upkeepTickAddsOneMinute() {
        UserController users = new UserController(user(0));
        RoutineController routines = new RoutineController(users, message -> { }, NO_SCHEDULER);

        routines.recordPlayTime();

        assertEquals(1, users.getPlayTimeMinutes());
    }

    @Test
    void upkeepUsesTheInjectedSchedulerAndStopsEveryTask() {
        UserController users = new UserController(user(0));
        FakeScheduler scheduler = new FakeScheduler();
        RoutineController routines = new RoutineController(users, message -> { }, scheduler);

        routines.startSchedulers();

        assertEquals(4, scheduler.tasks.size());
        assertTrue(scheduler.periods.contains(30_000L));
        assertTrue(scheduler.periods.contains(60_000L));
        // Last scheduled task is the one-minute play-time tick.
        scheduler.tasks.get(3).run();
        assertEquals(1, users.getPlayTimeMinutes());

        routines.stopSchedulers();
        assertEquals(4, scheduler.cancels.size());
    }

    @Test
    void levelUpRefillsVitalsAndNotifiesHealthAndEnergy() {
        User player = user(0);
        player.loseHealth(50);
        player.loseEnergy(50000);
        UserController users = new UserController(player);
        RecordingObserver observer = new RecordingObserver();
        users.addObserver(observer);

        users.addExperience(200); // level 1 needs 200 XP

        assertEquals(2, users.getLevel());
        assertEquals(100, users.getHealth());
        assertTrue(observer.saw(ChangeType.XP));
        assertTrue(observer.saw(ChangeType.HEALTH));
        assertTrue(observer.saw(ChangeType.ENERGY));
    }

    @Test
    void experienceWithoutLevelUpOnlyNotifiesXp() {
        UserController users = new UserController(user(0));
        RecordingObserver observer = new RecordingObserver();
        users.addObserver(observer);

        users.addExperience(10);

        assertEquals(1, users.getLevel());
        assertFalse(observer.saw(ChangeType.HEALTH));
        assertFalse(observer.saw(ChangeType.ENERGY));
    }

    @Test
    void successfulJobPublishesOneBatchedUpdate() {
        GameSession session = GameSession.start("Flow", programmer(), 0L);
        RecordingObserver observer = new RecordingObserver();
        session.observable().addObserver(observer);
        Job job = session.gameQueries().getJobsForCurrentIdentity().get(0);
        MiniGameResult game = MiniGameResult.quiz("Quiz", 100, 100, 20, List.of());

        Outcome<?> result = session.gameController().doJob(job, game);

        assertTrue(result.isSuccess());
        assertEquals(1, session.playerView().getTotalJobsCompleted());
        // Energy, pay, XP, reputation and job progress changed together, so
        // the batch flushes one full refresh instead of five partial ones.
        assertEquals(1, observer.events.size());
        assertNull(observer.events.get(0));
    }

    @Test
    void failedJobPublishesOneBatchedUpdate() {
        GameSession session = GameSession.start("Flow", programmer(), 0L);
        RecordingObserver observer = new RecordingObserver();
        session.observable().addObserver(observer);
        Job job = session.gameQueries().getJobsForCurrentIdentity().get(0);
        int energyBefore = session.playerView().getEnergy();
        MiniGameResult failed = MiniGameResult.quiz("Quiz", 10, 10, 30, List.of());

        Outcome<?> result = session.gameController().doJob(job, failed);

        assertFalse(result.isSuccess());
        assertEquals(0, session.playerView().getTotalJobsCompleted());
        assertTrue(session.playerView().getEnergy() < energyBefore);
        assertEquals(1, observer.events.size());
        assertNull(observer.events.get(0));
    }

    @Test
    void devPanelClampsValuesAndNeverCountsDemoMoneyAsEarned() {
        GameSession session = GameSession.start("Dev", programmer(), 0L);
        DeveloperController dev = session.developerController();
        PlayerView player = session.playerView();

        dev.setBalance(1_234);
        assertEquals(1_234, player.getBalance());
        // Demo money is not income, so achievement totals stay honest.
        assertEquals(0, player.getTotalMoneyEarned());

        dev.setHealth(500);
        assertEquals(Vitals.MAX_HEALTH, player.getHealth());
        dev.setHealth(-5);
        assertEquals(0, player.getHealth());

        dev.setEnergy(-5);
        assertEquals(0, player.getEnergy());
        dev.setEnergy(999_999);
        assertEquals(Vitals.MAX_ENERGY, player.getEnergy());

        dev.setLevel(0);
        assertEquals(1, player.getLevel());
        dev.setLevel(999);
        assertEquals(CareerProgress.MAX_LEVEL, player.getLevel());

        dev.setReputation(-1);
        assertEquals(0, player.getReputation());
        dev.setReputation(999);
        assertEquals(CareerProgress.MAX_REPUTATION, player.getReputation());

        dev.setJobBonus(999);
        assertEquals(JobRecord.MAX_JOB_BONUS, player.getJobBonus());

        dev.setPlaytimeMinutes(-4);
        assertEquals(0, player.getPlayTimeMinutes());
        dev.addPlaytimeMinutes(2);
        assertEquals(2, player.getPlayTimeMinutes());
    }

    @Test
    void economyControllerWorksThroughItsCommandPort() {
        ServiceProvider catalog = new ServiceProvider(List.of(
                new Service("Water", 5, 0, 500),
                new Service("Care", 20, 10, 0)));
        FakePurchaseTarget target = new FakePurchaseTarget(30);
        EconomyController economy = new EconomyController(target, catalog);

        assertTrue(economy.purchaseService("Water").isSuccess());
        assertEquals(25, target.getBalance());
        assertEquals(500, target.getEnergy());
        assertEquals(0, target.getHealth());

        assertTrue(economy.purchaseService("Care").isSuccess());
        assertEquals(5, target.getBalance());
        assertEquals(10, target.getHealth());
        assertFalse(economy.purchaseService("Care").isSuccess());
    }

    // No User/UserController behind this test: the command port is enough.
    private static final class FakePurchaseTarget implements PurchaseTarget {
        private int balance;
        private int health;
        private int energy;

        FakePurchaseTarget(int balance) {
            this.balance = balance;
        }

        @Override
        public boolean deductBalance(int amount) {
            if (amount > 0 && balance >= amount) {
                balance = balance - amount;
                return true;
            }
            return false;
        }

        @Override
        public boolean gainHealth(int amount) {
            if (amount <= 0) {
                return false;
            }
            health = health + amount;
            return true;
        }

        @Override
        public boolean gainEnergy(int amount) {
            if (amount <= 0) {
                return false;
            }
            energy = energy + amount;
            return true;
        }

        int getBalance() {
            return balance;
        }

        int getHealth() {
            return health;
        }

        int getEnergy() {
            return energy;
        }
    }

    @Test
    void purchaseDistinguishesUnknownServiceFromUnaffordableOne() {
        ServiceProvider catalog = new ServiceProvider(List.of(
                new Service("Water", 5, 0, 500),
                new Service("Gold", 500, 0, 1000)));
        UserController users = new UserController(user(50));
        EconomyController economy = new EconomyController(users, catalog);

        Outcome<Service> unknown = economy.purchaseService("Nope");
        assertFalse(unknown.isSuccess());
        assertEquals("Unknown service: Nope.", unknown.getMessage());
        assertTrue(unknown.getPayload().isEmpty());

        Outcome<Service> unaffordable = economy.purchaseService("Gold");
        assertFalse(unaffordable.isSuccess());
        assertTrue(unaffordable.getMessage().contains("Not enough balance"));
        assertTrue(unaffordable.getPayload().isEmpty());

        RecordingObserver observer = new RecordingObserver();
        users.addObserver(observer);
        Outcome<Service> bought = economy.purchaseService("Water");

        assertTrue(bought.isSuccess());
        assertEquals("Water", bought.getPayload().orElseThrow().getName());
        assertTrue(observer.saw(ChangeType.BALANCE));
        assertTrue(observer.saw(ChangeType.ENERGY));
        assertFalse(observer.saw(ChangeType.HEALTH));
    }
}

package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import controller.SessionTracker;
import model.career.Job;
import model.career.JobRewards;
import model.career.Profession;
import model.goals.Challenge;
import model.minigame.MiniGameResult;
import model.player.UpkeepTarget;
import model.player.User;
import org.junit.jupiter.api.Test;
import provider.ChallengeProvider;
import services.job.JobGate;
import utils.DurationText;
import utils.GameBalance;

// Service-level rules: job rewards, routine upkeep, and session summaries.
class GameRulesTest {

    private final JobService jobs = new JobService();

    // ----- JobService -----
    private User player() {
        User user = new User("Tester", new Profession("Programmer", "Programmer", "", 0), 0);
        user.addExperience(200); // level 2
        return user;
    }

    private Job job() {
        return new Job("Build a page", "", 1000, 100,
                new Profession("Programmer", "Programmer", "", 0), List.of());
    }

    @Test
    void paymentUsesLevelReputationAndJobBonus() {
        User user = player();
        user.grantStartingBonus(); // +2%
        user.gainReputation(200);  // clamped to +50%

        JobRewards rewards = jobs.calculateRewards(job(), user, null);

        // $1000 * (1 + 5% level + 50% rep + 2% job) = $1570
        assertEquals(1570, rewards.getFinalPayment());
        assertEquals(1000, rewards.getBasePayment());
        assertEquals(570, rewards.getBonusPayment());
        assertEquals(150, rewards.getExperience());
        assertEquals(2, rewards.getReputation());
    }

    @Test
    void miniGameScalesPayXpAndReputation() {
        User user = player();
        user.grantStartingBonus();
        user.gainReputation(200);
        MiniGameResult perfect = MiniGameResult.quiz("Quiz", 100, 100, 10,
                List.of("perfect round"));

        JobRewards rewards = jobs.calculateRewards(job(), user, perfect);

        // Base $1000 * 1.57 * 1.5 = $2355; XP 150 * 1.5 = 225;
        // job rep 2 + perfect rep 2 = 4.
        assertEquals(2355, rewards.getFinalPayment());
        assertEquals(225, rewards.getExperience());
        assertEquals(4, rewards.getReputation());
    }

    @Test
    void lowScoreStillPaysAtLeastOneDollar() {
        User user = player();
        MiniGameResult failed = MiniGameResult.quiz("Quiz", 10, 10, 90, List.of());

        JobRewards rewards = jobs.calculateRewards(job(), user, failed);

        // 0.6x multiplier still leaves a positive payout.
        assertEquals(630, rewards.getFinalPayment());
    }

    @Test
    void failedAttemptPenaltyDependsOnScore() {
        assertEquals(0, jobs.reputationPenalty(null));
        assertEquals(2, jobs.reputationPenalty(
                MiniGameResult.quiz("Quiz", 0, 0, 90, List.of())));
        assertEquals(1, jobs.reputationPenalty(
                MiniGameResult.quiz("Quiz", 20, 20, 90, List.of())));
    }

    @Test
    void jobGateExplainsEveryRejection() {
        JobGate gate = new JobGate();
        Profession programmer = new Profession("Programmer", "Programmer", "", 0);
        Profession doctor = new Profession("Doctor", "Doctor", "", 0);
        Job job = new Job("Build a page", "", 1000, 100, programmer, List.of("Web"));
        User noSkills = routinePlayer(0);

        assertEquals("No job selected.",
                gate.check(null, programmer, noSkills.getSkillSet(), 100, false).getReason());
        assertEquals("You are blocked. Rest and try later.",
                gate.check(job, programmer, noSkills.getSkillSet(), 100, true).getReason());
        assertEquals("Not available for your identity/skills. Learn more skills!",
                gate.check(job, doctor, noSkills.getSkillSet(), 100, false).getReason());
        assertEquals("Not available for your identity/skills. Learn more skills!",
                gate.check(job, programmer, noSkills.getSkillSet(), 100, false).getReason());
        assertEquals("Not enough energy (need 100, have 0). Eat or rest first.",
                gate.check(job, programmer, skillsWith("Web"), 0, false).getReason());
        assertTrue(gate.check(job, programmer, skillsWith("Web"), 100, false).isAllowed());
        assertFalse(gate.check(job, programmer, skillsWith("Web"), 100, true).isAllowed());
    }

    private static model.career.SkillSet skillsWith(String skill) {
        User user = new User("Tester", new Profession("Programmer", "Programmer", "", 0), 0);
        user.addSkill(skill);
        return user.getSkillSet();
    }

    @Test
    void nullInputsProduceZeroRewards() {
        JobRewards rewards = jobs.calculateRewards(null, null, null);

        assertEquals(0, rewards.getFinalPayment());
        assertEquals(0, rewards.getExperience());
        assertEquals(0, rewards.getReputation());
    }

    // ----- RoutineService -----
    private User routinePlayer(int balance) {
        return new User("Tester", new Profession("Programmer", "Programmer", "", balance), balance);
    }

    @Test
    void reachingZeroEnergyBlocksAndChargesPenalty() {
        User user = routinePlayer(500);
        user.loseEnergy(99_999); // 1 energy left
        RoutineService service = new RoutineService(new Random(1), System::currentTimeMillis);

        assertNull(service.processEnergyDecrease(user));

        assertEquals(0, user.getEnergy());
        assertEquals(500 - GameBalance.KNOCKOUT_MONEY_PENALTY, user.getBalance());
        assertTrue(user.isBlocked());
    }

    @Test
    void zeroEnergyDoesNothingOnTheNextTick() {
        User user = routinePlayer(500);
        user.loseEnergy(100_000);
        RoutineService service = new RoutineService(new Random(1), System::currentTimeMillis);

        assertNull(service.processEnergyDecrease(user));
        assertEquals(500, user.getBalance());
    }

    // A target with no User behind it: upkeep must work through the port.
    private static final class FakeUpkeep implements UpkeepTarget {
        private int health = 100;
        private int energy = 100_000;
        private int balance = 500;
        private long blockedUntil;
        private long lastSickness;

        @Override
        public int getHealth() {
            return health;
        }

        @Override
        public int getEnergy() {
            return energy;
        }

        @Override
        public int getBalance() {
            return balance;
        }

        @Override
        public boolean loseHealth(int amount) {
            health = Math.max(0, health - amount);
            return amount > 0;
        }

        @Override
        public boolean loseEnergy(int amount) {
            energy = Math.max(0, energy - amount);
            return amount > 0;
        }

        @Override
        public boolean withdraw(int amount) {
            if (amount > 0 && balance >= amount) {
                balance = balance - amount;
                return true;
            }
            return false;
        }

        @Override
        public void blockUntil(long time) {
            blockedUntil = time;
        }

        @Override
        public void markSicknessChecked(long time) {
            lastSickness = time;
        }

        @Override
        public long getLastSicknessTime() {
            return lastSickness;
        }
    }

    @Test
    void upkeepDrainsThroughTheUpkeepTargetPort() {
        RoutineService service = new RoutineService(new Random(1), () -> 1_000L);
        FakeUpkeep target = new FakeUpkeep();

        assertNull(service.processEnergyDecrease(target));

        assertEquals(99_999, target.getEnergy());
        assertEquals(500, target.getBalance());
    }

    @Test
    void knockoutWorksWithoutAUserAggregate() {
        RoutineService service = new RoutineService(new Random(1), () -> 1_000L);
        FakeUpkeep target = new FakeUpkeep();
        while (target.getEnergy() > 0) {
            service.processEnergyDecrease(target);
        }

        assertEquals(0, target.getEnergy());
        assertEquals(500 - GameBalance.KNOCKOUT_MONEY_PENALTY, target.getBalance());
        assertEquals(1_000L + GameBalance.KNOCKOUT_BLOCK_MILLIS, target.blockedUntil);
    }

    // ----- ChallengeService -----
    @Test
    void challengeCatalogHandsEachPlayerIndependentCopies() {
        ChallengeProvider provider = new ChallengeProvider();
        ChallengeService service = new ChallengeService(provider);
        User first = routinePlayer(0);
        User second = routinePlayer(0);

        service.initializeChallenges(first);
        service.initializeChallenges(second);
        service.updateChallengeProgress(first, Challenge.ChallengeType.COMPLETE_JOBS, 1);

        assertEquals(1, progressOf(first, "hustle_3"));
        assertEquals(0, progressOf(second, "hustle_3"));
    }

    private static int progressOf(User user, String challengeId) {
        for (Challenge challenge : user.getActiveChallenges()) {
            if (challenge.getId().equals(challengeId)) {
                return challenge.getCurrentProgress();
            }
        }
        return -1;
    }

    // ----- SessionTracker -----
    @Test
    void sessionTrackerSummarizesMinutesAndJobs() {
        long minute = DurationText.MILLIS_PER_MINUTE;
        SessionTracker session = new SessionTracker(0L, () -> minute * 5);

        assertEquals("Session: 5 minutes | Jobs: 0", session.summary());
        assertEquals(0, session.getJobsCompleted());

        session.recordCompletedJob();
        session.recordCompletedJob();

        assertEquals(2, session.getJobsCompleted());
        assertEquals("Session: 5 minutes | Jobs: 2", session.summary());
    }
}

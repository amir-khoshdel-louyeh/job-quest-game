package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import model.career.LearnableSkill;
import model.career.Profession;
import model.goals.Challenge;
import model.goals.ChallengeBook;
import model.minigame.MiniGameResult;
import model.minigame.QuizQuestion;
import model.minigame.QuizRound;
import model.player.BlockStatus;
import model.player.CareerProgress;
import model.player.PlayClock;
import model.player.User;
import model.player.Vitals;
import model.player.Wallet;
import model.store.Service;
import org.junit.jupiter.api.Test;
import provider.ServiceProvider;
import services.minigame.QuizScorer;

// Model rules: progression, resources, skills, services, and quiz rounds.
class ModelRulesTest {

    // ----- CareerProgress -----
    @Test
    void oneLargeXpGainLevelsUpRepeatedly() {
        CareerProgress progress = new CareerProgress();

        assertTrue(progress.addExperience(1200));

        // 1200 = 200 (level 2) + 400 (level 3) + 600 (level 4)
        assertEquals(4, progress.getLevel());
        assertEquals(0, progress.getExperience());
    }

    @Test
    void exactXpLevelsUpOnceWithNoRemainder() {
        CareerProgress progress = new CareerProgress();

        assertTrue(progress.addExperience(200));

        assertEquals(2, progress.getLevel());
        assertEquals(0, progress.getExperience());
    }

    @Test
    void noLevelUpReturnsFalse() {
        CareerProgress progress = new CareerProgress();

        assertFalse(progress.addExperience(10));
        assertFalse(progress.addExperience(0));
        assertEquals(1, progress.getLevel());
        assertEquals(10, progress.getExperience());
    }

    @Test
    void levelAndReputationStayInsideBounds() {
        CareerProgress progress = new CareerProgress();

        progress.gainReputation(500);
        assertEquals(CareerProgress.MAX_REPUTATION, progress.getReputation());

        progress.loseReputation(500);
        assertEquals(0, progress.getReputation());
    }

    // ----- Vitals, Wallet, PlayClock -----
    @Test
    void vitalsClampAndReportPercentages() {
        Vitals vitals = new Vitals();

        assertEquals(100, vitals.getHealthPercentage());
        assertEquals(100, vitals.getEnergyPercentage());

        vitals.loseHealth(30);
        assertEquals(70, vitals.getHealth());
        assertEquals(70, vitals.getHealthPercentage());

        assertFalse(vitals.loseHealth(0));
        vitals.loseHealth(1000);
        assertEquals(0, vitals.getHealth());
        assertEquals(0, vitals.getHealthPercentage());

        vitals.gainEnergy(1_000_000);
        assertEquals(Vitals.MAX_ENERGY, vitals.getEnergy());
        assertEquals(100, vitals.getEnergyPercentage());
    }

    @Test
    void walletEarnTracksTotalAndRejectsBadAmounts() {
        Wallet wallet = new Wallet(100);

        assertTrue(wallet.earn(50));
        assertEquals(150, wallet.getBalance());
        assertEquals(50, wallet.getTotalMoneyEarned());

        assertFalse(wallet.earn(0));
        assertEquals(150, wallet.getBalance());
        assertEquals(50, wallet.getTotalMoneyEarned());

        assertFalse(wallet.withdraw(1000));
        assertEquals(150, wallet.getBalance());
    }

    @Test
    void blockStatusUsesTheInjectedClock() {
        AtomicLong now = new AtomicLong(1_000);
        BlockStatus block = new BlockStatus(now::get);
        block.blockUntil(2_000);

        assertTrue(block.isBlocked());

        now.set(2_500);
        assertFalse(block.isBlocked());
    }

    @Test
    void playClockConvertsMillisToMinutes() {
        PlayClock clock = new PlayClock();

        clock.addMillis(90_000);
        clock.addMillis(-5);

        assertEquals(1, clock.getTotalMinutes());
        assertEquals(90_000, clock.getTotalMillis());
    }

    // ----- LearnableSkill -----
    private final Profession programmer = new Profession("Programmer", "Programmer", "", 300);
    private final Profession doctor = new Profession("Doctor", "Doctor", "", 500);
    private final LearnableSkill seo = new LearnableSkill(
            "Advanced SEO", "Boosts web jobs", 4000, 75, List.of(programmer));

    @Test
    void skillLockedUntilEnoughPlaytime() {
        User learner = new User("Tester", programmer, 0);

        assertEquals(LearnableSkill.Availability.LOCKED,
                seo.availabilityFor(learner.getSkillSet(), 74));
        assertEquals(1, seo.minutesUntilUnlocked(74));
        assertEquals(LearnableSkill.Availability.AVAILABLE,
                seo.availabilityFor(learner.getSkillSet(), 75));
        assertEquals(0, seo.minutesUntilUnlocked(75));
    }

    @Test
    void learnedSkillStaysMarkedLearned() {
        User learner = new User("Tester", programmer, 0);
        learner.addSkill("Advanced SEO");

        assertEquals(LearnableSkill.Availability.LEARNED,
                seo.availabilityFor(learner.getSkillSet(), 75));
    }

    @Test
    void challengeBookStoresItsOwnCopy() {
        Challenge original = new Challenge("daily", "Daily", "Do a job",
                Challenge.ChallengeType.COMPLETE_JOBS, 5, 0, 0);
        ChallengeBook book = new ChallengeBook();

        book.addChallenge(original);
        book.updateChallengeProgress(Challenge.ChallengeType.COMPLETE_JOBS, 1);

        // The caller's object must not be the stored (or progressing) one.
        assertEquals(0, original.getCurrentProgress());
        assertEquals(1, book.getActiveChallenges().get(0).getCurrentProgress());
    }

    @Test
    void skillOnlyBelongsToItsProfessions() {
        assertTrue(seo.isForIdentity(programmer));
        assertFalse(seo.isForIdentity(doctor));
    }

    // ----- Service -----
    @Test
    void serviceDescriptionIsDerivedFromEffects() {
        Service meal = new Service("Gourmet Meal", 200, 5, 25000);

        assertEquals("+25,000 Energy, +5 Health", meal.getDescription());
    }

    @Test
    void providerFiltersHealthAndEnergyCatalogs() {
        ServiceProvider provider = new ServiceProvider();

        assertTrue(provider.getHealthServices().stream().allMatch(Service::isHealthService));
        assertTrue(provider.getEnergyServices().stream().allMatch(Service::isEnergyService));
        assertEquals("Full Recovery Program", provider.getService("Full Recovery Program").getName());
    }

    // ----- QuizRound -----
    private final QuizScorer scorer = new QuizScorer();

    private QuizQuestion question(String id, int correctIndex) {
        return new QuizQuestion(id, "Prompt " + id,
                List.of("A", "B", "C"), correctIndex, "Because " + id);
    }

    private QuizRound round() {
        return new QuizRound("Quiz",
                List.of(question("q1", 0), question("q2", 1), question("q3", 2)), scorer);
    }

    @Test
    void answersAdvanceOnlyOncePerQuestion() {
        QuizRound round = round();

        assertEquals(0, round.getIndex());
        assertFalse(round.isAnswered(0));

        round.answer(0);
        round.answer(2); // second click ignored

        assertTrue(round.isAnswered(0));
        assertEquals(List.of(0, QuizRound.UNANSWERED, QuizRound.UNANSWERED), round.getAnswers());

        round.next();
        assertEquals(1, round.getIndex());
    }

    @Test
    void timeoutMarksUnansweredQuestionsAsSkipped() {
        QuizRound round = round();
        round.answer(0);

        MiniGameResult result = round.score(90);

        assertEquals(List.of(0, QuizRound.SKIPPED, QuizRound.SKIPPED), round.getAnswers());
        assertEquals(46, result.getScore()); // 33% accuracy + full speed
        assertEquals(33, result.getAccuracy());
        assertFalse(result.isPassed());
    }

    @Test
    void perfectRoundPasses() {
        QuizRound round = round();
        round.answer(0);
        round.next();
        round.answer(1);
        round.next();
        round.answer(2);

        MiniGameResult result = round.score(90);

        assertEquals(100, result.getScore());
        assertEquals(100, result.getAccuracy());
        assertTrue(result.isPassed());
        assertTrue(result.getHighlights().contains("perfect round 🌟"));
    }

    @Test
    void randomRoundNeverExceedsTheBank() {
        QuizRound round = QuizRound.random("Quiz",
                List.of(question("q1", 0), question("q2", 1)), 3, new Random(7), scorer);

        assertEquals(2, round.size());
        assertFalse(round.isLastQuestion());
    }
}

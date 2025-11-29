package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import model.career.Profession;
import model.goals.Challenge;
import model.player.PlayerView;
import model.player.User;
import org.junit.jupiter.api.Test;

// The view boundary: views get a PlayerView, never the mutable User aggregate.
class PlayerViewBoundaryTest {

    private Profession programmer() {
        return new Profession("Programmer", "Programmer", "", 500);
    }

    @Test
    void sessionExposesPlayerDataReadOnly() {
        GameSession session = GameSession.start("Ada", programmer(), 1000L);
        PlayerView player = session.playerView();

        assertEquals("Ada", player.getUsername());
        assertEquals("Programmer", player.getIdentityName());
        assertEquals(500, player.getBalance());
        // Starting bonus is applied exactly once by the composition root.
        assertEquals(1, player.getJobBonus());
        assertEquals(0, player.getTotalJobsCompleted());
        assertEquals(1, player.getLevel());
        // The composition root seeds story challenges before the view opens.
        assertFalse(session.gameQueries().getActiveChallenges().isEmpty());
    }

    @Test
    void sessionSharesOneAggregateWithItsControllers() {
        GameSession session = GameSession.start("Ada", programmer());

        assertNotNull(session.gameController());
        assertSame(session.playerView(), session.gameQueries().getPlayerView());

        // The read model and the command/notification controller share the
        // same player: a command is immediately visible through PlayerView.
        session.developerController().setBalance(777);
        assertEquals(777, session.playerView().getBalance());
    }

    @Test
    void challengeListsAreSnapshots() {
        GameSession session = GameSession.start("Ada", programmer(), 1000L);

        List<Challenge> active = session.gameQueries().getActiveChallenges();
        int size = active.size();
        active.clear();

        assertEquals(size, session.gameQueries().getActiveChallenges().size());
    }

    @Test
    void userKeepsItsComponentsPrivate() {
        for (String accessor : List.of("wallet", "vitals", "progress",
                "jobRecord", "playClock")) {
            assertThrows(NoSuchMethodException.class,
                    () -> User.class.getDeclaredMethod(accessor),
                    "User must not hand out its internal " + accessor);
        }
    }

    @Test
    void mutableImplementationTypesStayInsideTheirLayer() throws Exception {
        assertFalse(Modifier.isPublic(UserController.class.getModifiers()));
        assertFalse(Modifier.isPublic(Class.forName("controller.PlayerReadModel").getModifiers()));
        assertFalse(Modifier.isPublic(Class.forName("model.player.SkillBook").getModifiers()));
    }

    @Test
    void playerViewDeclaresNoMutations() {
        // getMethods() includes the role interfaces PlayerView composes.
        for (Method method : PlayerView.class.getMethods()) {
            assertFalse(Modifier.isStatic(method.getModifiers()),
                    "PlayerView should describe one player: " + method.getName());
            assertFalse(method.getReturnType().equals(void.class),
                    "PlayerView must not expose commands: " + method.getName());
            String name = method.getName();
            assertFalse(name.startsWith("set") || name.startsWith("add")
                    || name.startsWith("deduct") || name.startsWith("decrease")
                    || name.startsWith("gain") || name.startsWith("lose")
                    || name.startsWith("record") || name.startsWith("unblock")
                    || name.startsWith("withdraw"),
                    "PlayerView must not expose mutations: " + name);
        }
    }
}

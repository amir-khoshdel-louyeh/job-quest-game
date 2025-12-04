package main;

import javax.swing.SwingUtilities;

import controller.GameSessionFactory;
import controller.contract.GameStarter;
import provider.contract.IdentityCatalog;
import provider.IdentityProvider;
import provider.QuizProvider;
import services.minigame.QuizScorer;
import view.MainFrame;
import view.minigame.MiniGameLauncher;
import view.minigame.QuizLauncher;

// Starts the game and opens the main window.
// This is the application composition root: the only place that names the
// concrete catalogs, session factory, and mini-game adapters.
public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        IdentityCatalog identities = new IdentityProvider();
        GameStarter starter = new GameSessionFactory();
        MiniGameLauncher workGame = new QuizLauncher(new QuizProvider(), new QuizScorer());
        SwingUtilities.invokeLater(() -> new MainFrame(identities, starter, workGame));
    }
}

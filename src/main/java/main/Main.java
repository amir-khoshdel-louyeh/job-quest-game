package main;

import javax.swing.SwingUtilities;
import java.util.function.Supplier;

import controller.GameSessionFactory;
import controller.contract.GameStarter;
import provider.contract.ProfessionCatalog;
import provider.ProfessionRegistry;
import provider.QuizProvider;
import services.minigame.QuizScorer;
import view.MainFrame;
import view.contract.MiniGameLauncher;
import view.minigame.QuizLauncher;

// Starts the game and opens the main window.
// This is the application composition root: the only place that names the
// concrete catalogs, session factory, and mini-game adapters.
public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ProfessionCatalog professions = new ProfessionRegistry();
        GameStarter starter = new GameSessionFactory();
        Supplier<Long> clock = System::currentTimeMillis;
        MiniGameLauncher workGame = new QuizLauncher(new QuizProvider(), new QuizScorer(), clock);
        SwingUtilities.invokeLater(() -> new MainFrame(professions, starter, workGame));
    }
}

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides a centralized, single source of truth for available Identity options in the game.
 * This decouples the UI from the game's data configuration.
 */
public class IdentityOptionProvider {

    private static final List<IdentityOption> OPTIONS = new ArrayList<>();

    static {
        // To add a new character, just add a new line here.
        // The registration panel will automatically display it.
        OPTIONS.add(new IdentityOption("Freelancer", "Work online, earn flexible income.", 1000, "images/freelancer.png"));
        OPTIONS.add(new IdentityOption("Chef", "Cook meals and manage a restaurant.", 1200, "images/chef.png"));
        OPTIONS.add(new IdentityOption("Doctor", "Heal players and earn big money.", 1500, "images/doctor.png"));
    }

    public static List<IdentityOption> getAvailableOptions() {
        return Collections.unmodifiableList(OPTIONS);
    }
}
package model;

/**
 * Marker for a user's professional identity (Typist/Programmer/LogoDesigner/Doctor).
 * Work goes through JobController.doJob (rich JobResult DTO); this type only
 * identifies the profession for job eligibility and mini-game routing.
 *
 * <p>OOP Step 1: gives the abstraction real behavior ({@link #getProfessionName()})
 * instead of being an empty marker, so callers never use
 * {@code getClass().getSimpleName()} for game logic.</p>
 */
public interface Identity {
    /** Stable profession name, e.g. "Programmer". Used for display, DB and matching. */
    String getProfessionName();
}

package model;

/**
 * Model representing a skill that a user can learn or improve.
 *
 * Follows Single Responsibility Principle by modelling skill properties and simple helpers.
 */

public class Skill {
    private final String name;

    public Skill(String name) {
        // construct a simple skill with a name
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        this.name = name;
    }

    public String getName() {
        // return skill name
        return name;
    }

    /**
     * OOP Step 1: value-object equality by name, so
     * {@code List<Skill>.containsAll(...)} works on objects instead of
     * mapping everything to {@code List<String>} first.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() { return name.hashCode(); }

    @Override
    public String toString() { return name; }
}

package model;

/**
 * Model representing a skill that a user can learn or improve.
 *
 * Follows Single Responsibility Principle by modelling skill properties and simple helpers.
 */

public class Skill {
    private String name;

    public Skill(String name) {
        // construct a simple skill with a name
        this.name = name;
    }

    public String getName() {
        // return skill name
        return name;
    }
}

package model;

// This class represents a skill that the player can learn.
// Example: "Java Programming" or "Typing".
// It shows Encapsulation: the name is private, we use getter to read it.
public class Skill {
    // name of the skill
    private String name;

    // Constructor: create a new skill with a name
    public Skill(String name) {
        // simple check: name should not be empty
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        this.name = name;
    }

    // Getter: return skill name
    public String getName() {
        return name;
    }

    // This method checks if two skills are equal by name.
    // This helps us use contains() on ArrayList<Skill>.
    @Override
    public boolean equals(Object o) {
        // if same object, return true
        if (this == o) {
            return true;
        }
        // if o is not a Skill, return false
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // compare names
        Skill other = (Skill) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}

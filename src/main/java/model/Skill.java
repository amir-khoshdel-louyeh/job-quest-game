package model;

// A named skill the player can learn.
public class Skill {
    private String name;

    public Skill(String name) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Two skills count as equal when their names match.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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

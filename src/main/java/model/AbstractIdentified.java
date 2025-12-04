package model;

import java.util.Objects;

// Base class for entities whose identity is their id.
// This is a real abstract class, not a bag of helpers: subclasses must supply
// their id, while equality and hashing are one shared, final implementation.
public abstract class AbstractIdentified {

    // ----- Construction -----
    // Only subclasses may construct; subclasses live in other packages.
    protected AbstractIdentified() {
    }

    // Every concrete entity must say what identifies it.
    public abstract String getId();

    // Equal by id; the concrete class still has to be the same class.
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractIdentified other = (AbstractIdentified) obj;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}

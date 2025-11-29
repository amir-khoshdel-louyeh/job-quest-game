package observer;

// Gets told when the watched data changes.
public interface Observer {
    // A full refresh is just a change notification with no type.
    default void update() {
        update(null);
    }

    void update(ChangeType whatChanged);
}
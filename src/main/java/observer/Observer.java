package observer;

// Gets told when the watched data changes.
public interface Observer {
    void update();

    default void update(String whatChanged) {
        update();
    }
}
package observer;

// Gets told when the watched data changes.
public interface Observer {
    void update();

    void update(ChangeType whatChanged);
}
package observer;

import utils.Require;

// Subscribe-only view of a subject, handed to views.
// It deliberately implements nothing else, so a view cannot cast it to
// Subject and call notifyObservers().
public final class ReadOnlyObservable implements Observable {
    private final Subject subject;

    public ReadOnlyObservable(Subject subject) {
        this.subject = Require.notNull(subject, "subject");
    }

    @Override
    public void addObserver(Observer observer) {
        subject.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        subject.removeObserver(observer);
    }
}

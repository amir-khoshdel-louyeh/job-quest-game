package controller;

import observer.Observable;
import observer.Observer;
import observer.Subject;

// Subscribe-only view of a subject, handed to views.
// It deliberately implements nothing else, so a view cannot cast it to
// Subject and call notifyObservers().
final class SubscriptionPort implements Observable {
    private final Subject subject;

    SubscriptionPort(Subject subject) {
        this.subject = utils.Require.notNull(subject, "subject");
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

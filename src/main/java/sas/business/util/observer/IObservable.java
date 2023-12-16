package sas.business.util.observer;

public interface IObservable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notify(EObserverEventType eventType, String message);
}

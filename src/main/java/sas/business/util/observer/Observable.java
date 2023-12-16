package sas.business.util.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable implements IObservable {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notify(EObserverEventType eventType, String message) {
        observers.forEach(observer -> observer.notify(eventType, message));
    }
}

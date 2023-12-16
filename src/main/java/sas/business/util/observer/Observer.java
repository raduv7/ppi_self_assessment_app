package sas.business.util.observer;

public interface Observer {
    void notify(EObserverEventType eventType, String message);
}

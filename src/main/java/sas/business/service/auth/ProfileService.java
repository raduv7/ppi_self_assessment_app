package sas.business.service.auth;

import sas.business._interface.service.IProfileService;
import sas.business.util.observer.EObserverEventType;
import sas.business.util.observer.Observable;
import org.springframework.stereotype.Component;

@Component
public class ProfileService
        extends Observable
        implements IProfileService {
    @Override
    public void classifiedNotify() {
        notify(EObserverEventType.PROFILE_NOTIFICATION, null);
    }
}

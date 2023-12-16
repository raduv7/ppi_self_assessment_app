package sas.business.util.record;

import sas.business._interface.service.IProfileService;
import sas.business._interface.service.IUserService;
import sas.business._interface.util.record.IUserOperationRecord;
import sas.business.util.generator.TimestampGenerator;
import sas.business.util.observer.EObserverEventType;
import sas.business.util.observer.Observer;
import sas.infrastructure.repository.auth.IProfileRepository;
import sas.model.entity.auth.Profile;
import sas.model.entity.auth.User;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserOperationRecord implements Observer, IUserOperationRecord {
    @Autowired private IProfileRepository profileRepository;
    @Autowired private IUserService userService;
    @Autowired private IProfileService profileService;

    private static final long MAX_TIME = 60 * 1000; // 1 minute, this obviously should be configured after user testing

    private final Map<String, PairProfileTimestamp> username2ProfileMap = new HashMap<>();
    private final Map<String, Profile> profileMap = new HashMap<>();

    @PostConstruct
    public void init() {
        profileService.addObserver(this);
        userService.addObserver(this);
        loadProfiles();
    }
    private void loadProfiles() {
        profileRepository.findAll().forEach(profile -> profileMap.put(profile.getName(), profile));
    }

    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void removeOldRecords() {
        username2ProfileMap.forEach((username, pair) -> {
            if(System.currentTimeMillis() - pair.getTimestamp().getTime() > MAX_TIME) {
                username2ProfileMap.remove(username);
            }
        });
    }

    @Override
    public void notify(EObserverEventType eventType, String message) {
        if(eventType == EObserverEventType.PROFILE_NOTIFICATION) {
            if(message == null) {
                profileMap.clear();
                loadProfiles();
            }
            else {
                Optional<Profile> optionalProfile = profileRepository.findByName(message);
                if(optionalProfile.isPresent()) {
                    Profile profile = optionalProfile.get();
                    profileMap.put(profile.getName(), profile);
                }
            }
        }

        if(eventType == EObserverEventType.USER_NOTIFICATION) {
            if(message != null) {
                username2ProfileMap.remove(message);
            }
        }
    }

    public Profile getProfile(User actor) {
        PairProfileTimestamp pair = username2ProfileMap.get(actor.getUsername());

        if(pair == null) {
            Profile profile = profileMap.get(actor.getProfileName());

            if(profile == null) {
                throw new RuntimeException("Profile not found in record");
            }

            pair = new PairProfileTimestamp(profile, TimestampGenerator.now());
            username2ProfileMap.put(actor.getUsername(), pair);
            return profile;
        }
        else {
            pair.setTimestamp(TimestampGenerator.now());
            return pair.getProfile();
        }
    }

    @Data
    @AllArgsConstructor
    private static class PairProfileTimestamp {
        private Profile profile;
        private Timestamp timestamp;
    }
}

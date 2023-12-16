package sas.infrastructure.seeder;

import sas.api.controller.AuthenticationController;
import sas.business._interface.service.IProfileService;
import sas.infrastructure.repository.auth.IProfileRepository;
import sas.infrastructure.repository.auth.IUserRepository;
import sas.model.dto.auth.user.UserChangePasswordDto;
import sas.model.dto.auth.user.UserCreateDto;
import sas.model.entity.auth.Operation;
import sas.model.entity.auth.Profile;
import sas.model.entity.auth.User;
import sas.model.entity.auth._enum.EOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public abstract class AbstractDatabaseSeeder {
    @Autowired private IProfileRepository profileRepository;
    @Autowired private IUserRepository userRepository;
    @Autowired private IProfileService profileService;
    @Autowired private AuthenticationController authenticationController;

    private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseSeeder.class.getName());
    private static final List<Operation> allRights = List.of(
            new Operation(EOperationType.NONE, "ALL")
    );

    protected void insertSystemEntriesIfNotPresent() {
        if(userRepository.findByUsername("system").isEmpty()) {
            try {
                insertProfileSystem();
                insertUserSystem();
            } catch (NoSuchElementException e) {
                logger.error("Error while inserting system entries: {}", e.getMessage(), e);
            }
        }
    }

    private void insertProfileSystem() throws NoSuchElementException {
        List<Operation> operations_system = new ArrayList<Operation>();
        Profile profile_system = new Profile();
        profile_system.setName("system");
        profile_system.setRights(operations_system);
        profileRepository.save(profile_system);
    }

    protected void insertProfilesIfNotPresent() {
        if(this.profileRepository.count() <= 1) {
            try {
                insertProfileAdministrator();
                insertProfileReserver();
            } catch (NoSuchElementException e) {
                logger.error("Error while inserting profiles: {}", e.getMessage(), e);
            }

            profileService.classifiedNotify();
        }
    }

    protected void insertProfileAdministrator() {
        List<Operation> operations = new ArrayList<>();
        Profile profile = new Profile();
        profile.setName("administrator");
        profile.setRights(operations);
        profileRepository.save(profile);
    }
    protected void insertProfileReserver() {
        List<Operation> operations = new ArrayList<>();
        Profile profile = new Profile();
        profile.setName("reserver");
        profile.setRights(operations);
        profileRepository.save(profile);
    }

    private void insertUserSystem() throws NoSuchElementException {
        User user_system = new User();
        user_system.setUsername("system");
        user_system.setPassword("absolutely_not_a_valid_password");
        user_system.setSalt("absolutely_not_a_valid_salt");
        user_system.setFirstName("system");
        user_system.setLastName("system");
        user_system.setEmailAddress("system");
        user_system.setProfile(profileRepository.findByName("system").orElseThrow());
        userRepository.save(user_system);
    }

    protected void insertUsersIfNotPresent() {
        if(this.userRepository.count() <= 1) {
            try {
                insertUser("a1", "administrator");
                insertUser("a2", "administrator");
                insertUser("r1", "reserver");
                insertUser("r2", "reserver");
            } catch (NoSuchElementException e) {
                logger.error("Error while inserting users: {}", e.getMessage(), e);
            }
        }
    }

    protected void insertUser(String name, String profileName) {
        signUpUser(name, profileName);
        User user = userRepository.findByUsername(name).orElseThrow();
        changeUserPassword(user, name, "temporary_password");
        user = userRepository.findByUsername(name).orElseThrow();
        changeUserPassword(user, "temporary_password", name);
    }

    private void signUpUser(String name, String profileName) {
        UserCreateDto userSignUpDto = new UserCreateDto();
        userSignUpDto.setUsername(name);
        userSignUpDto.setPassword(name);
        userSignUpDto.setFirstName(name);
        userSignUpDto.setLastName(name);
        userSignUpDto.setEmailAddress(name);
        userSignUpDto.setProfileName(profileName);
        authenticationController.signUp(userSignUpDto);
    }

    private void changeUserPassword(User user, String oldPassword, String newPassword) {
        UserChangePasswordDto userChangePasswordDto = new UserChangePasswordDto();
        userChangePasswordDto.setOldPassword(oldPassword);
        userChangePasswordDto.setNewPassword(newPassword);
        authenticationController.changePassword(user, userChangePasswordDto);
    }
}

package sas.business._interface.util.record;

import sas.model.entity.auth.Profile;
import sas.model.entity.auth.User;

public interface IUserOperationRecord {
    Profile getProfile(User actor);
}

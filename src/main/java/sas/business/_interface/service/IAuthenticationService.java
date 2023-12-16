package sas.business._interface.service;

import sas.model.dto.auth.user.UserChangePasswordDto;
import sas.model.dto.auth.user.UserCreateDto;
import sas.model.dto.auth.user.UserSignInDto;
import sas.model.entity.auth.User;
import sas.model.exception_handling.FirstSignInException;

import javax.naming.AuthenticationException;

public interface IAuthenticationService {
    String signIn(UserSignInDto userSignInDto) throws AuthenticationException, FirstSignInException;
    String signUp(UserCreateDto userSignUpDto);
    void signOut(User actor);
    String changePassword(User user, UserChangePasswordDto userChangePasswordDto) throws AuthenticationException;
}

package sas.business._interface.util.auth;

import javax.naming.AuthenticationException;

public interface IJwtUtils {
    String validateToken(String token) throws AuthenticationException;
}

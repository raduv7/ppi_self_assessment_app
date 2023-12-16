package sas.business.util.auth;

import sas.business._interface.util.auth.IJwtUtils;
import sas.business.util.record.AuthenticationRecord;
import sas.model.entity.auth.User;
import javax.naming.AuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtils implements IJwtUtils {
    @Autowired
    private AuthenticationRecord authenticationRecord;
    // The secret key used to sign/validate JWT tokens
    private Key SECRET_KEY;

    // Token expiration time (in milliseconds)
//    private static final long EXPIRATION_TIME_MS = 15 * 60 * 1000; // 15 minutes
    private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000; // 1 day

    // Inject the secret key from the application.properties file
    @Value("${jwtSecretKey}")
    private void setSecretKey(String jwtSecretKey) {
        SECRET_KEY = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    // Method to generate a JWT token from a username and a timestamp
    public String generateToken(String username, Timestamp timestamp) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        long lastLogin;
        if(timestamp == null) {
            lastLogin = 0;
        }
        else {
            lastLogin = timestamp.getTime();
        }

        return Jwts.builder()
                .setSubject(username)
                .claim("timestamp", lastLogin)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Method to generate a JWT token from a user object
    public String generateToken(User user) {
        authenticationRecord.setNewAction(user.getUsername());
        return generateToken(user.getUsername(), authenticationRecord.getLastAction(user.getUsername()));
    }

    // Method to validate a JWT token and return the username if valid
    public String validateToken(String token) throws AuthenticationException {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);

            String username = claimsJws.getBody().getSubject();
            Long date = claimsJws.getBody().get("timestamp", Long.class);
            Timestamp lastAction = internalGetTimestamp(date);

            if(lastAction == null || !Objects.equals(lastAction.getTime(), authenticationRecord.getLastAction(username).getTime())) {
                throw new AuthenticationException("Last action is invalid!");
            }

            return username;
        }
        catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    private Timestamp internalGetTimestamp(long date) {
        if(date == 0) {
            return null;
        }
        else {
            return new Timestamp(date);
        }
    }
}

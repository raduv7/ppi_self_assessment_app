package sas.business.util.auth;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class HashingUtils {
    @Value("${passwordPepper}")
    private String PEPPER;

    private static final String SHA_384 = "SHA-384";
    public String hash(String password, String salt) throws ServiceException {
        String saltedAndPepperedPassword = PEPPER + password + salt;
        return hashWithSHA384(saltedAndPepperedPassword);
    }

    public String generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashWithSHA384(String input) throws ServiceException {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(SHA_384);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Hashing algorithm not found.");
        }

        byte[] hashedBytes = messageDigest.digest(input.getBytes());

        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}

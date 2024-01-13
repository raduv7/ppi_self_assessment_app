package sas.model.exceptionHandling;

import org.hibernate.service.spi.ServiceException;

public class FirstSignInException extends ServiceException {
    public FirstSignInException(String message) {
        super(message);
    }
}

package sas.model.exception_handling;

import org.hibernate.service.spi.ServiceException;

public class FirstSignInException extends ServiceException {
    public FirstSignInException(String message) {
        super(message);
    }
}

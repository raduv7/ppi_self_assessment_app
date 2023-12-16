package sas.business.mapper.exception_handling;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.ResponseEntity;

public class ExceptionMapper {

    private static ResponseEntity<String> toResponseEntity(Exception exception) {

        if(exception instanceof ServiceException) {

        }

        return ResponseEntity.internalServerError().build();
    }
}

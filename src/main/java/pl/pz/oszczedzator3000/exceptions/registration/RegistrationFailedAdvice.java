package pl.pz.oszczedzator3000.exceptions.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class RegistrationFailedAdvice {

    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<ExceptionDto> registrationFailedHandler(RegistrationFailedException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

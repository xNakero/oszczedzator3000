package pl.pz.oszczedzator3000.exceptions.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class InvalidRegistrationDataAdvice {

    @ExceptionHandler(InvalidRegistrationDataException.class)
    public ResponseEntity<ExceptionDto> invalidRegistrationDataHandler(InvalidRegistrationDataException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

package pl.pz.oszczedzator3000.exceptions.goalanalyser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidDatesHandler {

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<String> invalidDatesHandler(InvalidDatesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

package pl.pz.oszczedzator3000.exceptions.goalanalyser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class InvalidDatesAdvice {

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<ExceptionDto> invalidDatesHandler(InvalidDatesException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

package pl.pz.oszczedzator3000.exceptions.token;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class InvalidTokenAdvice {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionDto> invalidTokenHandler(InvalidTokenException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

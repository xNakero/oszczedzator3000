package pl.pz.oszczedzator3000.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class UserNotAllowedAdvice {

    @ExceptionHandler(UserNotAllowedException.class)
    public ResponseEntity<ExceptionDto> userNotFoundHandler(UserNotAllowedException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}

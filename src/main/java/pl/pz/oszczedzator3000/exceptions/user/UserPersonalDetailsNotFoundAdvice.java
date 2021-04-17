package pl.pz.oszczedzator3000.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserPersonalDetailsNotFoundAdvice {

    @ExceptionHandler(UserPersonalDetailsNotFoundException.class)
    public ResponseEntity<String> UserPersonalDetailsNotFoundHandler(
            UserPersonalDetailsNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}

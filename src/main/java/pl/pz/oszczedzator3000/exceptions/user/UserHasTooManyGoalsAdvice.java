package pl.pz.oszczedzator3000.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserHasTooManyGoalsAdvice {

    @ExceptionHandler(UserHasTooManyGoalsException.class)
    public ResponseEntity<String> userHasTooManyGoalsHandler(UserHasTooManyGoalsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}

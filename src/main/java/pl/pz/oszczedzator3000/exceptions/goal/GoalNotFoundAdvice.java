package pl.pz.oszczedzator3000.exceptions.goal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GoalNotFoundAdvice {

    @ExceptionHandler(GoalNotFoundException.class)
    public ResponseEntity<String> GoalNotFoundHandler(GoalNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

package pl.pz.oszczedzator3000.exceptions.goal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class GoalNotFoundAdvice {

    @ExceptionHandler(GoalNotFoundException.class)
    public ResponseEntity<ExceptionDto> GoalNotFoundHandler(GoalNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}

package pl.pz.oszczedzator3000.exceptions.expense;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class ExpenseNotFoundAdvice {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ExceptionDto> ExpenseNotFoundHandler(ExpenseNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}

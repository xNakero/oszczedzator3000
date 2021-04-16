package pl.pz.oszczedzator3000.exceptions.expense;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExpenseNotFoundAdvice {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<String> ExpenseNotFoundHandler(ExpenseNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

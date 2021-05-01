package pl.pz.oszczedzator3000.exceptions.expenseoptimiser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class NoCategoriesAdvice {

    @ExceptionHandler(NoCategoriesException.class)
    public ResponseEntity<ExceptionDto> NoCategoriesHandler(NoCategoriesException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

package pl.pz.oszczedzator3000.exceptions.goalanalyser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pz.oszczedzator3000.dto.exception.ExceptionDto;

@RestControllerAdvice
public class ContributionHigherThanPriceAdvice {

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ExceptionDto> contributionHigherThanPriceHandler(InvalidValueException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

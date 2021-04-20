package pl.pz.oszczedzator3000.exceptions.goalanalyser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContributionHigherThanPriceAdvice {

    @ExceptionHandler(ContributionHigherThanPriceException.class)
    public ResponseEntity<String> contributionHigherThanPriceHandler(ContributionHigherThanPriceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

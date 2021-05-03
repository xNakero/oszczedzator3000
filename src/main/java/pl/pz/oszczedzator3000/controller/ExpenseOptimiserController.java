package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.expenseoptimiser.ExpenseOptimiserResponseDto;
import pl.pz.oszczedzator3000.dto.expenseoptimiser.FiltrationExpenseOptimiserRequestDto;
import pl.pz.oszczedzator3000.service.ExpenseOptimiserService;

@RestController
@RequestMapping("api/v1")
public class ExpenseOptimiserController {

    private ExpenseOptimiserService expenseOptimiserService;

    @Autowired
    public ExpenseOptimiserController(ExpenseOptimiserService expenseOptimiserService) {
        this.expenseOptimiserService = expenseOptimiserService;
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @GetMapping("users/{userId}/optimiser")
    public ResponseEntity<ExpenseOptimiserResponseDto> getOptimiserResults(@PathVariable Long userId,
                                                                           @RequestBody FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto) {
        return new ResponseEntity<>(
                expenseOptimiserService.getOptimiserResults(userId, filtrationExpenseOptimiserRequestDto),
                HttpStatus.OK);
    }
}

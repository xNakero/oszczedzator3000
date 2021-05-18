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
@CrossOrigin(origins = "http://localhost:3000")
public class ExpenseOptimiserController {

    private final ExpenseOptimiserService expenseOptimiserService;

    @Autowired
    public ExpenseOptimiserController(ExpenseOptimiserService expenseOptimiserService) {
        this.expenseOptimiserService = expenseOptimiserService;
    }

    @PostMapping("optimiser")
    public ResponseEntity<ExpenseOptimiserResponseDto> getOptimiserResults(@RequestBody FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto) {
        return new ResponseEntity<>(
                expenseOptimiserService.getOptimiserResults(filtrationExpenseOptimiserRequestDto),
                HttpStatus.OK);
    }
}

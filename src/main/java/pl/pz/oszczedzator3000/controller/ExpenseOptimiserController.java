package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("optimiser")
    public ResponseEntity<ExpenseOptimiserResponseDto> getOptimiserResults(@RequestParam Long userId,
                                                                           @RequestBody FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto) {
        return new ResponseEntity<>(
                expenseOptimiserService.getOptimiserResults(userId, filtrationExpenseOptimiserRequestDto),
                HttpStatus.OK);
    }
}

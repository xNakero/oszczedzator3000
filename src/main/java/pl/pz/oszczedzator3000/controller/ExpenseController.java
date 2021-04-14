package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.ExpenseFilterRequestDto;
import pl.pz.oszczedzator3000.dto.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.ExpenseResponseDto;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.service.ExpenseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("users/{userId}/expenses")
    public ResponseEntity<Page<ExpenseResponseDto>> getUsersExpensePage(@PathVariable Long userId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ExpenseResponseDto> expenses = expenseService.getUserExpensePage(userId, page, size);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("users/{userId}/expenses/filtered")
    public ResponseEntity<Page<ExpenseResponseDto>> getExpensesFiltered(@PathVariable Long userId,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                                             @RequestParam(value = "name", required = false) String name,
                                                             @RequestBody(required = false) ExpenseFilterRequestDto expenseFilterRequestDto) {
        return new ResponseEntity<>(expenseService.getUserExpensePageFiltered(userId,
                page, size, name, expenseFilterRequestDto) ,HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/expenses")
    public ResponseEntity<String> postExpense(@PathVariable Long userId,
                                              @RequestBody ExpenseRequestDto expenseRequestDto) {
        Optional<Expense> e = expenseService.postExpense(userId, expenseRequestDto);
        if (e.isPresent()) {
            return new ResponseEntity<>("Expense was created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/{userId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long userId,
                                                 @PathVariable Long expenseId,
                                                 @RequestBody ExpenseRequestDto expenseRequestDto) {
        return new ResponseEntity<>(expenseService.updateExpense(userId, expenseId, expenseRequestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/expenses/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long userId,
                                                 @PathVariable Long expenseId) {
        expenseService.deleteExpense(userId, expenseId);
        return new ResponseEntity<>("Expense was deleted", HttpStatus.OK);
    }

}

package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.expense.ExpenseFilterRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseResponseDto;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.service.ExpenseService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @GetMapping("users/{userId}/expenses")
    public ResponseEntity<Page<ExpenseResponseDto>> getUsersExpensePage(@PathVariable Long userId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ExpenseResponseDto> expenses = expenseService.getUserExpensePage(userId, page, size);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @GetMapping("users/{userId}/expenses/filtered")
    public ResponseEntity<Page<ExpenseResponseDto>> getExpensesFiltered(@PathVariable Long userId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                                        @RequestBody(required = false) ExpenseFilterRequestDto expenseFilterRequestDto) {
        return new ResponseEntity<>(expenseService.getUserExpensePageFiltered(userId,
                page, size, expenseFilterRequestDto) ,HttpStatus.OK);
    }

    @PreAuthorize(value = "#userId == authentication.details")
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

    @PreAuthorize(value = "#userId == authentication.details")
    @PutMapping("/users/{userId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long userId,
                                                 @PathVariable Long expenseId,
                                                 @RequestBody ExpenseRequestDto expenseRequestDto) {
        return new ResponseEntity<>(expenseService.updateExpense(userId, expenseId, expenseRequestDto),
                HttpStatus.OK);
    }

    @PreAuthorize(value = "#userId == authentication.details")
    @DeleteMapping("/users/{userId}/expenses/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long userId,
                                                 @PathVariable Long expenseId) {
        expenseService.deleteExpense(userId, expenseId);
        return new ResponseEntity<>("Expense was deleted", HttpStatus.OK);
    }

}

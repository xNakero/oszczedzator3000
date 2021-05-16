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
@CrossOrigin(origins = "http://localhost:3000")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @GetMapping("expenses")
    public ResponseEntity<Page<ExpenseResponseDto>> getUsersExpensePage(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ExpenseResponseDto> expenses = expenseService.getUserExpensePage(page, size);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("expenses/filtered")
    public ResponseEntity<Page<ExpenseResponseDto>> getExpensesFiltered(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                                        @RequestBody(required = false) ExpenseFilterRequestDto expenseFilterRequestDto) {
        return new ResponseEntity<>(
                expenseService.getUserExpensePageFiltered(page, size, expenseFilterRequestDto) ,HttpStatus.OK);
    }

    @PostMapping("expenses")
    public ResponseEntity<String> postExpense(@RequestBody ExpenseRequestDto expenseRequestDto) {
        Optional<Expense> e = expenseService.postExpense(expenseRequestDto);
        if (e.isPresent()) {
            return new ResponseEntity<>("Expense was created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("expenses/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long expenseId,
                                                 @RequestBody ExpenseRequestDto expenseRequestDto) {
        return new ResponseEntity<>(expenseService.updateExpense(expenseId, expenseRequestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("expenses/{expenseId}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return new ResponseEntity<>("Expense was deleted", HttpStatus.OK);
    }

}

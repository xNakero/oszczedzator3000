package pl.pz.oszczedzator3000.controller;

import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.repo.ExpenseRepository;

import java.util.Optional;
@RestController
public class ExpenseController {
    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }


    @GetMapping("/expense/details/{id}")
    public Optional<Expense> getExpense(@PathVariable Long id) {
        return expenseRepository.findById(id);
    }

    @GetMapping("/expense/all")
    public Iterable<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    @DeleteMapping("expense/delete/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
    }
}

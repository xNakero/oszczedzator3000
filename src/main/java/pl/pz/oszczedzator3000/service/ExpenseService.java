package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.dto.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.ExpenseResponseDto;
import pl.pz.oszczedzator3000.exceptions.ExpenseNotFoundException;
import pl.pz.oszczedzator3000.exceptions.UserNotFoundException;
import pl.pz.oszczedzator3000.mapper.ExpenseMapper;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.ExpenseRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private ExpenseMapper expenseMapper;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,
                          ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseMapper = expenseMapper;
    }

    public Page<ExpenseResponseDto> getUserExpensePage(Long userId, int page, int size) {
        Optional<User> user = userRepository.findById(userId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("date").descending());
        if (user.isPresent()) {
            return expenseRepository.findAllByUser(user.get(), pageRequest).map(expenseMapper::mapToExpenseResponseDto);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    public Optional<Expense> postExpense(Long userId, ExpenseRequestDto expenseRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        Expense expense = expenseMapper.mapToExpense(expenseRequestDto);
        if (user.isPresent() && !expenseRequestDto.hasNullAttribute()) {
            expense.setUser(user.get());
            expenseRepository.save(expense);
            return Optional.of(expense);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Transactional
    public ExpenseResponseDto updateExpense(Long userId, Long expenseId, ExpenseRequestDto expenseRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException(expenseId);
        }

        Expense expense = expenseOptional.get();
        if (expenseRequestDto.getCategory() != null) {
            expense.setCategory(expenseRequestDto.getCategory());
        }
        if (expenseRequestDto.getDate() != null) {
            expense.setDate(expenseRequestDto.getDate());
        }
        if (expenseRequestDto.getValue() != 0.0) {
            expense.setValue(expenseRequestDto.getValue());
        }
        if (expenseRequestDto.getName() != null) {
            expense.setName(expenseRequestDto.getName());
        }
        return expenseMapper.mapToExpenseResponseDto(expense);
    }

    public void deleteExpense(Long userId, Long expenseId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException(expenseId);
        }
        expenseRepository.delete(expenseOptional.get());
    }
}

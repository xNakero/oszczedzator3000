package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.dto.expense.ExpenseFilterRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseResponseDto;
import pl.pz.oszczedzator3000.exceptions.expense.ExpenseNotFoundException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotAllowedException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.mapper.ExpenseMapper;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.ExpenseRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,
                          ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseMapper = expenseMapper;
    }

    public Page<ExpenseResponseDto> getUserExpensePage(int page, int size) {
        User user = getUserPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("date")
                .descending());
        return expenseRepository.findAllByUser(user, pageRequest)
                .map(expenseMapper::mapToExpenseResponseDto);
    }

    @Transactional
    public Page<ExpenseResponseDto> getUserExpensePageFiltered(int page,
                                                               int size,
                                                               ExpenseFilterRequestDto expenseFilterRequestDto) {
        User user = getUserPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("date").descending());

        List<ExpenseResponseDto> list = expenseRepository.streamAllByUser(user)
                .filter(expense -> expenseFilterRequestDto.getName() == null ||
                        expenseFilterRequestDto.getName().equals(expense.getName()))
                .filter(expense -> expenseFilterRequestDto.getCategory() == null ||
                        expense.getCategory().equals(expenseFilterRequestDto.getCategory()))
                .filter(expense -> expense.getValue() >= expenseFilterRequestDto.getMinValue())
                .filter(expense -> expenseFilterRequestDto.getMaxValue() == 0.0 ||
                        expense.getValue() <= expenseFilterRequestDto.getMaxValue())
                .filter(expense -> expenseFilterRequestDto.getStartDate() == null ||
                        expense.getDate().compareTo(expenseFilterRequestDto.getStartDate()) >= 0)
                .filter(expense -> expenseFilterRequestDto.getEndDate() == null ||
                        expense.getDate().compareTo(expenseFilterRequestDto.getEndDate()) <= 0)
                .map(expenseMapper::mapToExpenseResponseDto)
                .collect(Collectors.toList());

        List<ExpenseResponseDto> pageToReturn = new ArrayList<>();
        int startIndex = page * size;
        int endIndex = startIndex + size;

        if (list.size() < endIndex) {
            endIndex = list.size();
        }

        for (int i = startIndex; i < endIndex; i++) {
            pageToReturn.add(list.get(i));
        }

        return new PageImpl<>(pageToReturn, pageRequest, list.size());
    }

    public Optional<Expense> postExpense(ExpenseRequestDto expenseRequestDto) {
        User user = getUserPrincipal();
        Expense expense = expenseMapper.mapToExpense(expenseRequestDto);
        if (!expenseRequestDto.hasInvalidAttributes()) {
            expense.setUser(user);
            expenseRepository.save(expense);
            return Optional.of(expense);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public ExpenseResponseDto updateExpense(Long expenseId, ExpenseRequestDto expenseRequestDto) {
        User user = getUserPrincipal();
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));
        if (!user.getExpenses().contains(expense)) {
            throw new UserNotAllowedException();
        }
        if (expenseRequestDto.getCategory() != null) {
            expense.setCategory(expenseRequestDto.getCategory());
        }
        if (expenseRequestDto.getDate() != null) {
            expense.setDate(expenseRequestDto.getDate());
        }
        if (expenseRequestDto.getValue() > 0.0) {
            expense.setValue(expenseRequestDto.getValue());
        }
        if (expenseRequestDto.getName() != null) {
            expense.setName(expenseRequestDto.getName());
        }
        return expenseMapper.mapToExpenseResponseDto(expense);
    }

    public void deleteExpense(Long expenseId) {
        User user = getUserPrincipal();
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() ->new ExpenseNotFoundException(expenseId));
        if (!user.getExpenses().contains(expense)) {
            throw new UserNotAllowedException();
        }
        expenseRepository.delete(expense);
    }

    private User getUserPrincipal() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }
}

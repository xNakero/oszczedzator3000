package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.dto.expense.ExpenseFilterRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseResponseDto;
import pl.pz.oszczedzator3000.exceptions.expense.ExpenseNotFoundException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.mapper.ExpenseMapper;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.ExpenseRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

    @Transactional
    public Page<ExpenseResponseDto> getUserExpensePageFiltered(Long userId,
                                                               int page,
                                                               int size,
                                                               String name,
                                                               ExpenseFilterRequestDto expenseFilterRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("date").descending());

        List<ExpenseResponseDto> list = expenseRepository.streamAllByUser(user.get())
                .filter(expense -> name == null || name.equals(expense.getName()))
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

        for(int i = startIndex; i < endIndex; i++) {
            pageToReturn.add(list.get(i));
        }

        return new PageImpl<>(pageToReturn, pageRequest, list.size());
    }


    public Optional<Expense> postExpense(Long userId, ExpenseRequestDto expenseRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        Expense expense = expenseMapper.mapToExpense(expenseRequestDto);
        if(user.isEmpty()) {
            throw new UserNotFoundException(userId);
        } else if (!expenseRequestDto.hasInvalidAttributes()) {
            expense.setUser(user.get());
            expenseRepository.save(expense);
            return Optional.of(expense);
        } else {
            return Optional.empty();
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
        if (expenseRequestDto.getValue() > 0.0) {
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

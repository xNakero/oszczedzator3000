package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.dto.expense.ExpenseResponseDto;
import pl.pz.oszczedzator3000.dto.goal.GoalRequestDto;
import pl.pz.oszczedzator3000.dto.goal.GoalResponseDetailsDto;
import pl.pz.oszczedzator3000.dto.goal.GoalResponseDto;
import pl.pz.oszczedzator3000.exceptions.expense.ExpenseNotFoundException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.mapper.ExpenseMapper;
import pl.pz.oszczedzator3000.mapper.GoalMapper;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.ExpenseRepository;
import pl.pz.oszczedzator3000.repository.GoalRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;
import java.time.temporal.ChronoUnit;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private GoalRepository goalRepository;
    private UserRepository userRepository;
    private ExpenseRepository expenseRepository;
    private GoalMapper goalMapper;
    private ExpenseMapper expenseMapper;

    @Autowired
    public GoalService(GoalRepository goalRepository, UserRepository userRepository, ExpenseRepository expenseRepository, GoalMapper goalMapper, ExpenseMapper expenseMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.goalMapper = goalMapper;
        this.expenseMapper = expenseMapper;
    }

    public Page<GoalResponseDto> getUserGoalPage(Long userId, int page, int size) {
        Optional<User> user = userRepository.findById(userId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("targetDate").descending());
        if (user.isPresent()) {
            return goalRepository.findAllByUser(user.get(), pageRequest).map(goalMapper::mapToGoalResponseDto);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Transactional
    public GoalResponseDetailsDto getUserGoalPageDetails(Long userId, Long goalId, int page, int size) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        if (goalOptional.isEmpty()) {
            throw new ExpenseNotFoundException(goalId);
        }
        GoalResponseDetailsDto goalResponseDetailsDto = goalMapper.mapToGoalResponseDetailsDto(goalOptional.get());
        if (goalResponseDetailsDto.getCategory() != null && goalResponseDetailsDto.getTargetDate() != null &&
                goalResponseDetailsDto.getPrice() > 0.0 && goalResponseDetailsDto.getName() != null) {
            goalResponseDetailsDto.setToTargetDate((int)ChronoUnit.DAYS.between(goalResponseDetailsDto.getTargetDate(), LocalDateTime.now()));
            goalResponseDetailsDto.setAmount(goalResponseDetailsDto.getPrice() - expenseRepository
                    .streamAllByUser(user.get())
                    .map(expenseMapper::mapToExpenseResponseDto)
                    .collect(Collectors.toList())
                    .stream()
                    .mapToDouble(ExpenseResponseDto::getValue)
                    .sum());
            goalResponseDetailsDto.setPossible(goalResponseDetailsDto.getToTargetDate() > 0 && goalResponseDetailsDto.getAmount() > 0);

        }
        return goalResponseDetailsDto;
    }

    public Optional<Goal> postGoal(Long userId, GoalRequestDto goalRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        Goal expense = goalMapper.mapToGoal(goalRequestDto);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        } else if (!goalRequestDto.hasInvalidAttributes()) {
            expense.setUser(user.get());
            goalRepository.save(expense);
            return Optional.of(expense);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public GoalResponseDto updateGoal(Long userId, Long goalId, GoalRequestDto goalRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        if (goalOptional.isEmpty()) {
            throw new ExpenseNotFoundException(goalId);
        }

        Goal goal = goalOptional.get();
        if (goalRequestDto.getCategory() != null) {
            goal.setCategory(goalRequestDto.getCategory());
        }
        if (goalRequestDto.getTargetDate() != null) {
            goal.setTargetDate(goalRequestDto.getTargetDate());
        }
        if (goalRequestDto.getPrice() > 0.0) {
            goal.setPrice(goalRequestDto.getPrice());
        }
        if (goalRequestDto.getName() != null) {
            goal.setName(goalRequestDto.getName());
        }
        return goalMapper.mapToGoalResponseDto(goal);
    }

    public void deleteGoal(Long userId, Long goalId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        if (goalOptional.isEmpty()) {
            throw new ExpenseNotFoundException(goalId);
        }
        goalRepository.delete(goalOptional.get());
    }
}

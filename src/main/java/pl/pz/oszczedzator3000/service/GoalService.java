package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.dto.goal.GoalFilterRequestDto;
import pl.pz.oszczedzator3000.dto.goal.GoalRequestDto;
import pl.pz.oszczedzator3000.dto.goal.GoalResponseDto;
import pl.pz.oszczedzator3000.exceptions.goal.GoalNotFoundException;
import pl.pz.oszczedzator3000.exceptions.user.UserHasTooManyGoalsException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotAllowedException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.mapper.GoalMapper;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.GoalRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalMapper goalMapper;

    @Autowired
    public GoalService(GoalRepository goalRepository, UserRepository userRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
    }

    public Page<GoalResponseDto> getUserGoalPage(int page, int size) {
        User user = getUserPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("targetDate").descending());
        return goalRepository.findAllByUser(user, pageRequest).map(goalMapper::mapToGoalResponseDto);
    }

    @Transactional
    public Page<GoalResponseDto> getUserGoalPageFiltered(int page,
                                                         int size,
                                                         String name,
                                                         GoalFilterRequestDto goalFilterRequestDto) {
        User user = getUserPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("targetDate")
                .descending());

        List<GoalResponseDto> list = goalRepository.streamAllByUser(user)
                .filter(expense -> name == null || name.equals(expense.getName()))
                .filter(expense -> goalFilterRequestDto.getCategory() == null ||
                        expense.getCategory().equals(goalFilterRequestDto.getCategory()))
                .map(goalMapper::mapToGoalResponseDto)
                .collect(Collectors.toList());

        List<GoalResponseDto> pageToReturn = new ArrayList<>();
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

    @Transactional
    public Optional<Goal> postGoal(GoalRequestDto goalRequestDto) {
        User user = getUserPrincipal();
        if (goalRepository.streamAllByUser(user).count() >= 10) {
            throw new UserHasTooManyGoalsException();
        }
        if (!goalRequestDto.hasInvalidAttributes()) {
            Goal goal = goalMapper.mapToGoal(goalRequestDto);
            goal.setUser(user);
            goalRepository.save(goal);
            return Optional.of(goal);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public GoalResponseDto updateGoal(Long goalId, GoalRequestDto goalRequestDto) {
        User user = getUserPrincipal();
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId));
        if (!user.getGoals().contains(goal)) {
            throw new UserNotAllowedException();
        }
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

    public void deleteGoal(Long goalId) {
        User user = getUserPrincipal();
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId));
        if (!user.getGoals().contains(goal)) {
            throw new UserNotAllowedException();
        }
        goalRepository.delete(goal);
    }

    private User getUserPrincipal() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }
}

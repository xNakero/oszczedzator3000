package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private GoalRepository goalRepository;
    private UserRepository userRepository;
    private GoalMapper goalMapper;

    @Autowired
    public GoalService(GoalRepository goalRepository, UserRepository userRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.goalMapper = goalMapper;
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
    public Page<GoalResponseDto> getUserGoalPageFiltered(Long userId,
                                                               int page,
                                                               int size,
                                                               String name,
                                                         GoalFilterRequestDto goalFilterRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("targetDate").descending());

        List<GoalResponseDto> list = goalRepository.streamAllByUser(user.get())
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

        for(int i = startIndex; i < endIndex; i++) {
            pageToReturn.add(list.get(i));
        }

        return new PageImpl<>(pageToReturn, pageRequest, list.size());
    }

    @Transactional
    public Optional<Goal> postGoal(Long userId, GoalRequestDto goalRequestDto) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if(goalRepository.streamAllByUser(user.get()).count() >= 10){
                throw new UserHasTooManyGoalsException(userId);
            }
        } else {
            throw new UserNotFoundException(userId);
        }
        if (!goalRequestDto.hasInvalidAttributes()) {
            Goal goal = goalMapper.mapToGoal(goalRequestDto);
            goal.setUser(user.get());
            goalRepository.save(goal);
            return Optional.of(goal);
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
            throw new GoalNotFoundException(goalId);
        }
        if(!user.get().getGoals().contains(goalOptional.get())){
            throw new UserNotAllowedException(userId);
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
            throw new GoalNotFoundException(goalId);
        }
        if(!user.get().getGoals().contains(goalOptional.get())){
            throw new UserNotAllowedException(userId);
        }
        goalRepository.delete(goalOptional.get());
    }
}

package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.pz.oszczedzator3000.dto.expense.ExpenseFilterRequestDto;
import pl.pz.oszczedzator3000.dto.expense.ExpenseResponseDto;
import pl.pz.oszczedzator3000.dto.goal.GoalFilterRequestDto;
import pl.pz.oszczedzator3000.dto.goal.GoalRequestDto;
import pl.pz.oszczedzator3000.dto.goal.GoalResponseDto;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.service.GoalService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class GoalController {

    private GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("users/{userId}/goals")
    public ResponseEntity<Page<GoalResponseDto>> getUsersGoalsPage(@PathVariable Long userId,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<GoalResponseDto> goals = goalService.getUserGoalPage(userId, page, size);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @GetMapping("users/{userId}/goals/filtered")
    public ResponseEntity<Page<GoalResponseDto>> getGoalsFiltered(@PathVariable Long userId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                                                        @RequestParam(value = "name", required = false) String name,
                                                                     @RequestBody(required = false) GoalFilterRequestDto goalFilterRequestDto) {
        return new ResponseEntity<>(goalService.getUserGoalPageFiltered(userId,
                page, size, name, goalFilterRequestDto) ,HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/goals")
    public ResponseEntity<String> postGoals(@PathVariable Long userId,
                                            @RequestBody GoalRequestDto goalRequestDto) {
        Optional<Goal> g = goalService.postGoal(userId, goalRequestDto);
        if (g.isPresent()) {
            return new ResponseEntity<>("Goal was created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/{userId}/goals/{goalId}")
    public ResponseEntity<GoalResponseDto> updateGoal(@PathVariable Long userId,
                                                         @PathVariable Long goalId,
                                                         @RequestBody GoalRequestDto goalRequestDto) {
        return new ResponseEntity<>(goalService.updateGoal(userId, goalId, goalRequestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/goals/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long userId,
                                             @PathVariable Long goalId) {
        goalService.deleteGoal(userId, goalId);
        return new ResponseEntity<>("Goal was deleted", HttpStatus.OK);
    }

}

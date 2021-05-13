package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("goals")
    public ResponseEntity<Page<GoalResponseDto>> getUsersGoalsPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<GoalResponseDto> goals = goalService.getUserGoalPage(page, size);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @GetMapping("goals/filtered")
    public ResponseEntity<Page<GoalResponseDto>> getGoalsFiltered(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                                  @RequestParam(value = "name", required = false) String name,
                                                                  @RequestBody(required = false) GoalFilterRequestDto goalFilterRequestDto) {
        return new ResponseEntity<>(
                goalService.getUserGoalPageFiltered(page, size, name, goalFilterRequestDto), HttpStatus.OK);
    }

    @PostMapping("goals")
    public ResponseEntity<String> postGoals(@RequestBody GoalRequestDto goalRequestDto) {
        Optional<Goal> g = goalService.postGoal(goalRequestDto);
        if (g.isPresent()) {
            return new ResponseEntity<>("Goal was created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("goals/{goalId}")
    public ResponseEntity<GoalResponseDto> updateGoal(@PathVariable Long goalId,
                                                      @RequestBody GoalRequestDto goalRequestDto) {
        return new ResponseEntity<>(goalService.updateGoal(goalId, goalRequestDto),
                HttpStatus.OK);
    }

    @DeleteMapping("goals/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return new ResponseEntity<>("Goal was deleted", HttpStatus.OK);
    }

}

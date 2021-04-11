package pl.pz.oszczedzator3000.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.repo.GoalRepository;

import java.util.Optional;

@RestController
public class GoalController {
    private final GoalRepository goalRepository;

    public GoalController(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }


    @GetMapping("/goal/details/{id}")
    public Optional<Goal> getGoal(@PathVariable Long id) {
        return goalRepository.findById(id);
    }

    @GetMapping("/goal/all")
    public Iterable<Goal> getGoals() {
        return goalRepository.findAll();
    }

    @DeleteMapping("goal/delete/{id}")
    public void deleteGoal(@PathVariable Long id) {
        goalRepository.deleteById(id);
    }
}

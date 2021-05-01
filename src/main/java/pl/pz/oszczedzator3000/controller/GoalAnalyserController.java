package pl.pz.oszczedzator3000.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pz.oszczedzator3000.dto.goalanalyser.GoalAnalyserRequestDto;
import pl.pz.oszczedzator3000.dto.goalanalyser.GoalAnalyserResponseDto;
import pl.pz.oszczedzator3000.service.GoalAnalyserService;

@RestController
@RequestMapping("api/v1")
public class GoalAnalyserController {

    private GoalAnalyserService goalAnalyserService;

    @Autowired
    public GoalAnalyserController(GoalAnalyserService goalAnalyserService) {
        this.goalAnalyserService = goalAnalyserService;
    }

    @GetMapping("users/{userId}/goals/{goalId}/analyser")
    public ResponseEntity<GoalAnalyserResponseDto> getGoalAnalyzerResult(@PathVariable Long userId,
                                                                         @PathVariable Long goalId,
                                                                         @RequestBody GoalAnalyserRequestDto goalAnalyserRequestDto) {
        return new ResponseEntity<>(goalAnalyserService.getGoalAnalyserResult(userId, goalId, goalAnalyserRequestDto),
                HttpStatus.OK);
    }
}

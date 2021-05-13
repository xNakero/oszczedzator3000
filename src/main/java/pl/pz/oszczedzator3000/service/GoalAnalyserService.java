package pl.pz.oszczedzator3000.service;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.dto.goalanalyser.GoalAnalyserRequestDto;
import pl.pz.oszczedzator3000.dto.goalanalyser.GoalAnalyserResponseDto;
import pl.pz.oszczedzator3000.exceptions.goal.GoalNotFoundException;
import pl.pz.oszczedzator3000.exceptions.goalanalyser.InvalidValueException;
import pl.pz.oszczedzator3000.exceptions.goalanalyser.InvalidDatesException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.GoalRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class GoalAnalyserService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    @Autowired
    public GoalAnalyserService(UserRepository userRepository, GoalRepository goalRepository) {
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
    }

    //basic version of analyser where I assume that every month has 30 days
    public GoalAnalyserResponseDto getGoalAnalyserResult(Long goalId,
                                                         GoalAnalyserRequestDto goalAnalyserRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> new GoalNotFoundException(goalId));
        if(goalAnalyserRequestDto.getStartDate() == null || goalAnalyserRequestDto.getEndDate() == null) {
            throw new InvalidDatesException("At least one of dates is null.");
        }
        if (goalAnalyserRequestDto.getEndDate().compareTo(goalAnalyserRequestDto.getStartDate()) <= 0) {
            throw new InvalidDatesException("End date happens before start date.");
        }
        if (goalAnalyserRequestDto.getEndDate().compareTo(goal.getTargetDate()) >= 0) {
            throw new InvalidDatesException("End date is the same or after target date.");
        }
        if (LocalDate.now().compareTo(goal.getTargetDate()) >= 0) {
            throw new InvalidDatesException("Target date has already passed");
        }
        if (goalAnalyserRequestDto.getInitialContribution() >= goal.getPrice()) {
            throw new InvalidValueException("Initial Contribution is higher than price");
        }
        if (goalAnalyserRequestDto.getInitialContribution() < 0) {
            throw new InvalidValueException("Initial contribution is negative");
        }

        GoalAnalyserResponseDto response = new GoalAnalyserResponseDto();
        double moneyToCollect = goal.getPrice() - goalAnalyserRequestDto.getInitialContribution();
        moneyToCollect = Precision.round(moneyToCollect, 2);
        response.setMoneyToCollect(moneyToCollect);

        long daysBetweenEndAndStartDates = ChronoUnit.DAYS
                .between(goalAnalyserRequestDto.getStartDate(), goalAnalyserRequestDto.getEndDate());
        double spendingSum = user.getExpenses().stream()
                .filter(e -> goalAnalyserRequestDto.getStartDate() == null ||
                        e.getDate().compareTo(goalAnalyserRequestDto.getStartDate()) >= 0)
                .filter(e -> goalAnalyserRequestDto.getEndDate() == null ||
                        e.getDate().compareTo(goalAnalyserRequestDto.getEndDate()) <= 0)
                .mapToDouble(Expense::getValue)
                .sum();
        double earnedInTimePeriod = ((double) daysBetweenEndAndStartDates / Constants.MONTH_DAYS_THEORETICAL) *
                user.getUserPersonalDetails().getSalary();

        if (earnedInTimePeriod > spendingSum) {
            response.setCanAchieve(true);
            double possibleSavingsPerDay = (earnedInTimePeriod - spendingSum)
                    * ((double) 1 / daysBetweenEndAndStartDates);
            possibleSavingsPerDay = Precision.round(possibleSavingsPerDay, 2);
            response.setAverageDailyPossibleSavings(possibleSavingsPerDay);
        } else {
            response.setCanAchieve(false);
        }

        long daysUntilTargetDate = ChronoUnit.DAYS
                .between(LocalDate.now(), goal.getTargetDate());
        double savingsPerDayNeeded = ((double) 1 / daysUntilTargetDate) * moneyToCollect;
        savingsPerDayNeeded = Precision.round(savingsPerDayNeeded, 2);
        response.setAverageDailyNecessarySavings(savingsPerDayNeeded);

        response.setCanAchieveBeforeEndDate(
                response.getAverageDailyPossibleSavings() > response.getAverageDailyNecessarySavings());

        return response;
    }
}

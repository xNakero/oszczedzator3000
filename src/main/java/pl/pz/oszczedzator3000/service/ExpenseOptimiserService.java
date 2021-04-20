package pl.pz.oszczedzator3000.service;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.dto.expenseoptimiser.ExpenseOptimiserResponseDto;
import pl.pz.oszczedzator3000.dto.expenseoptimiser.ExpenseOptimiserResponseSingleDataDto;
import pl.pz.oszczedzator3000.dto.expenseoptimiser.FiltrationExpenseOptimiserRequestDto;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.exceptions.user.UserPersonalDetailsNotFoundException;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.UserPersonalDetails;
import pl.pz.oszczedzator3000.model.enums.Category;
import pl.pz.oszczedzator3000.repository.UserPersonalDetailsRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpenseOptimiserService {

    private UserRepository userRepository;
    private UserPersonalDetailsRepository userPersonalDetailsRepository;

    @Autowired
    public ExpenseOptimiserService(UserRepository userRepository,
                                   UserPersonalDetailsRepository userPersonalDetailsRepository) {
        this.userRepository = userRepository;
        this.userPersonalDetailsRepository = userPersonalDetailsRepository;

    }

    @Transactional
    public ExpenseOptimiserResponseDto getOptimiserResults(Long userId,
                                                           FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserPersonalDetails userPersonalDetails = userPersonalDetailsRepository.findById(userId)
                .orElseThrow(() -> new UserPersonalDetailsNotFoundException(userId));
        List<User> users = filterUsers(filtrationExpenseOptimiserRequestDto, userPersonalDetails);
        users.remove(user);
        Set<ExpenseOptimiserResponseSingleDataDto> similarUsersStatistics =
                getSimilarUsersStatistics(users, filtrationExpenseOptimiserRequestDto);
        Set<ExpenseOptimiserResponseSingleDataDto> userStatistics =
                getUserStatistics(filtrationExpenseOptimiserRequestDto, user);
        ExpenseOptimiserResponseDto response = new ExpenseOptimiserResponseDto();
        response.setSimilarUsersData(similarUsersStatistics);
        response.setUserData(userStatistics);
        return response;
    }

    private Set<ExpenseOptimiserResponseSingleDataDto> getSimilarUsersStatistics(List<User> users,
                                                                                 FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto) {
        Set<ExpenseOptimiserResponseSingleDataDto> similarUsersStatistics = new HashSet<>();
        Set<Expense> expenses = new HashSet<>();
        users.forEach(u -> expenses.addAll(u.getExpenses()));
        if (filtrationExpenseOptimiserRequestDto.getCategories() != null) {
            for (Category c : filtrationExpenseOptimiserRequestDto.getCategories()) {
                Optional<ExpenseOptimiserResponseSingleDataDto> statisticsForCategory =
                        getStatisticsForCategory(filtrationExpenseOptimiserRequestDto, expenses, c);
                statisticsForCategory.ifPresent(similarUsersStatistics::add);
            }
        }

        return similarUsersStatistics;
    }

    private Set<ExpenseOptimiserResponseSingleDataDto> getUserStatistics(FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto,
                                                                         User user) {

        Set<ExpenseOptimiserResponseSingleDataDto> userStatistics = new HashSet<>();
        Set<Expense> expenses = user.getExpenses();
        if (filtrationExpenseOptimiserRequestDto.getCategories() != null) {
            for (Category c : filtrationExpenseOptimiserRequestDto.getCategories()) {
                Optional<ExpenseOptimiserResponseSingleDataDto> statisticsForCategory =
                        getStatisticsForCategory(filtrationExpenseOptimiserRequestDto, expenses, c);
                statisticsForCategory.ifPresent(userStatistics::add);
            }
        }
        return userStatistics;
    }

    private List<User> filterUsers(FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto,
                                   UserPersonalDetails details) {
        return userRepository.streamAllBy()
                .filter(u -> !filtrationExpenseOptimiserRequestDto.isSalary() ||
                        (u.getUserPersonalDetails().getSalary() >= details.getSalary() - Constants.SALARY_DEVIATION &&
                                u.getUserPersonalDetails().getSalary() <= details.getSalary() + Constants.SALARY_DEVIATION))
                .filter(u -> !filtrationExpenseOptimiserRequestDto.isProfession() ||
                        u.getUserPersonalDetails().getProfession().equals(details.getProfession()))
                .filter(u -> !filtrationExpenseOptimiserRequestDto.isAge() ||
                        (u.getUserPersonalDetails().getSalary() >= details.getAge() - Constants.AGE_DEVIATION &&
                                u.getUserPersonalDetails().getAge() <= details.getAge() + Constants.AGE_DEVIATION))
                .filter(u -> !filtrationExpenseOptimiserRequestDto.isSex() ||
                        u.getUserPersonalDetails().getSex().equals(details.getSex()))
                .filter(u -> !filtrationExpenseOptimiserRequestDto.isKids() ||
                        u.getUserPersonalDetails().getKids() == details.getKids())
                .filter(u -> !filtrationExpenseOptimiserRequestDto.isRelationshipStatus() ||
                        u.getUserPersonalDetails().getRelationshipStatus().equals(details.getRelationshipStatus()))
                .collect(Collectors.toList());
    }

    private Optional<ExpenseOptimiserResponseSingleDataDto> getStatisticsForCategory(FiltrationExpenseOptimiserRequestDto filtrationExpenseOptimiserRequestDto,
                                                                                     Set<Expense> expenses,
                                                                                     Category category) {
        Set<Expense> expenseSet = expenses.stream()
                .filter(expense -> filtrationExpenseOptimiserRequestDto.getStartDate() == null ||
                        expense.getDate().compareTo(filtrationExpenseOptimiserRequestDto.getStartDate()) >= 0)
                .filter(expense -> filtrationExpenseOptimiserRequestDto.getEndDate() == null ||
                        expense.getDate().compareTo(filtrationExpenseOptimiserRequestDto.getEndDate()) <= 0)
                .filter(expense -> expense.getCategory() == null || expense.getCategory().equals(category))
                .collect(Collectors.toSet());
        OptionalDouble averageSpent = expenseSet.stream()
                .mapToDouble(Expense::getValue)
                .average();
        long expenseCount = expenseSet.size();
        if (averageSpent.isPresent()) {
            ExpenseOptimiserResponseSingleDataDto singleDataDto = new ExpenseOptimiserResponseSingleDataDto();
            singleDataDto.setCategory(category);
            singleDataDto.setExpenseCount(expenseCount);
            singleDataDto.setAverageSpent(Precision.round(averageSpent.getAsDouble(), 2));
            return Optional.of(singleDataDto);
        }
        return Optional.empty();
    }
}






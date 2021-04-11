package pl.pz.oszczedzator3000;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pz.oszczedzator3000.model.Enum.CategoryEnum;
import pl.pz.oszczedzator3000.model.Enum.RoleEnum;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repo.ExpenseRepository;
import pl.pz.oszczedzator3000.repo.GoalRepository;
import pl.pz.oszczedzator3000.repo.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class Runner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;

    @Autowired
    public Runner(UserRepository userRepository, ExpenseRepository expenseRepository, GoalRepository goalRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.goalRepository = goalRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();
        expenseRepository.deleteAll();
        goalRepository.deleteAll();

        Set<Role> authorities = new HashSet<>();
        for (RoleEnum roleEnum : RoleEnum.values()) {
            authorities.add(new Role(roleEnum));
        }

        List<User> users = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            users.add(new User("User" + i, "haslo", "user" + i + "@gmail.com", authorities, new HashSet<>(), new HashSet<>(), LocalDateTime.now()));
        }
        userRepository.saveAll(users);

        for (int i = 1; i < users.size() * 10; i++) {
            CategoryEnum categoryEnum = CategoryEnum.values()[new Random().nextInt(CategoryEnum.values().length)];
            User user = users.get(new Random().nextInt(users.size()));

            user.getExpenses().add(new Expense(0L, categoryEnum, new Random().nextInt(users.size()) + " expense for " + categoryEnum.toString().toLowerCase(),
                    new Random().nextDouble() * 12000, LocalDate.now(), user));
            user.getGoals().add(new Goal(0L, categoryEnum, "It's " + new Random().nextInt(users.size()) + " " + categoryEnum.toString().toLowerCase() + " goal",
                    new Random().nextDouble() * 8000, LocalDateTime.now(), user));

            expenseRepository.saveAll(user.getExpenses());
            goalRepository.saveAll(user.getGoals());
        }

    }
}

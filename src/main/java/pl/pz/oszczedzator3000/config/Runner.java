package pl.pz.oszczedzator3000.config;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.model.*;
import pl.pz.oszczedzator3000.model.enums.*;
import pl.pz.oszczedzator3000.repository.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class Runner implements CommandLineRunner {

    private static final Random random = new Random();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;
    private final UserPersonalDetailsRepository userPersonalDetailsRepository;

    @Autowired
    public Runner(UserRepository userRepository, RoleRepository roleRepository, PasswordConfig passwordConfig, ExpenseRepository expenseRepository, GoalRepository goalRepository, UserPersonalDetailsRepository userPersonalDetailsRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.expenseRepository = expenseRepository;
        this.goalRepository = goalRepository;
        this.userPersonalDetailsRepository = userPersonalDetailsRepository;
    }

    private UserPersonalDetails generateUserPersonalDetails(User user) {
        UserPersonalDetails userPersonalDetails = new UserPersonalDetails();
        userPersonalDetails.setUserId(user.getUserId());
        userPersonalDetails.setSalary(random.nextInt(10000));
        userPersonalDetails.setProfession(randomEnum(Profession.class));
        userPersonalDetails.setAge(random.nextInt(60) + 18);
        userPersonalDetails.setSex(randomEnum(Sex.class));
        userPersonalDetails.setRelationshipStatus(randomEnum(RelationshipStatus.class));
        userPersonalDetails.setKids(random.nextInt(5));
        userPersonalDetails.setUser(user);
        return userPersonalDetails;
    }

    private Goal generateGoal() {
        Goal goal = new Goal();
        goal.setCategory(randomEnum(Category.class));
        goal.setName("Goal");
        goal.setPrice(Precision.round(ThreadLocalRandom.current().nextDouble(100, 10000), 2));
        goal.setTargetDate(getLocalDate(7, 12).plusDays(365L));
        return goal;
    }

    private Expense generateExpense() {
        Expense expense = new Expense();
        expense.setCategory(randomEnum(Category.class));
        expense.setName("Expense");
        expense.setValue(Precision.round(ThreadLocalRandom.current().nextDouble(1, 10000), 2));
        expense.setDate(getLocalDate(1, 6).minusDays(365L));
        return expense;
    }

    private <T extends Enum<?>> T randomEnum(Class<T> c) {
        return c.getEnumConstants()[random.nextInt(c.getEnumConstants().length)];
    }

    private LocalDate getLocalDate(int fromMonth, int toMonth) {
        return LocalDate
                .ofEpochDay(ThreadLocalRandom
                        .current()
                        .nextLong(LocalDate.of(2021, fromMonth, 1).toEpochDay(),
                                LocalDate.of(2021, toMonth, 30).toEpochDay()));
    }

    private User generateUser(Role role) {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user.setRoles(Stream.of(role).collect(Collectors.toSet()));
        user.setEnabled(true);
        return user;
    }

    private List<User> generateUsers(Role role, Set<User> roles) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = generateUser(role);
            user.setUsername(user.getUsername() + i);
            roles.add(user);
            userRepository.save(user);
            users.add(user);
        }
        return users;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (AppRole appRole : AppRole.values()) {
            pl.pz.oszczedzator3000.model.Role role1 = new pl.pz.oszczedzator3000.model.Role();
            role1.setName("ROLE_" + appRole.name());
            roleRepository.save(role1);
        }

        User user1 = new User();
        pl.pz.oszczedzator3000.model.Role role1 = roleRepository.findById(1L).orElseThrow(RuntimeException::new);
        user1.setUsername("admin");
        user1.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user1.setRoles(Stream.of(role1).collect(Collectors.toSet()));
        Set<User> roles = role1.getUsers();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(user1);
        userRepository.save(user1);
        roleRepository.save(role1);
        pl.pz.oszczedzator3000.model.Role role2 = roleRepository.findById(2L).orElseThrow(RuntimeException::new);

        List<User> users = generateUsers(role2, roles);

        roles = role2.getUsers();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roleRepository.save(role2);

        for (User u : users) {
            UserPersonalDetails upd = generateUserPersonalDetails(u);
            upd.setUser(u);
            userPersonalDetailsRepository.save(upd);
        }

//        for (User u : users) {
//            userRepository.save(u);
//        }

        for (int i = 0; i < 500; i++) {
            Goal g = generateGoal();
            g.setName(g.getName() + i);
            g.setUser(users.get(random.nextInt(users.size())));
            goalRepository.save(g);
        }

        for (int i = 0; i < 10000; i++) {
            Expense e = generateExpense();
            e.setName(e.getName() + i);
            e.setUser(users.get(random.nextInt(users.size())));
            expenseRepository.save(e);
        }
    }
}

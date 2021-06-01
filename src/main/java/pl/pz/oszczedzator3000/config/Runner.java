package pl.pz.oszczedzator3000.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.AppRole;
import pl.pz.oszczedzator3000.repository.RoleRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class Runner implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordConfig passwordConfig;

    @Autowired
    public Runner(UserRepository userRepository, RoleRepository roleRepository, PasswordConfig passwordConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
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

        // USER 1
        pl.pz.oszczedzator3000.model.Role role2 = roleRepository.findById(2L).orElseThrow(RuntimeException::new);
        User user2 = new User();
        user2.setUsername("user");
        user2.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user2.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user2.setEnabled(true);

        // USER 2
        User user3 = new User();
        user3.setUsername("user3");
        user3.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user3.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user3.setEnabled(true);

        // USER 3
        User user4 = new User();
        user4.setUsername("user4");
        user4.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user4.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user4.setEnabled(true);

        // USER 4
        User user5 = new User();
        user5.setUsername("user5");
        user5.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user5.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user5.setEnabled(true);

        // USER 5
        User user6 = new User();
        user6.setUsername("user6");
        user6.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user6.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user6.setEnabled(true);

        // USER 6
        User user7 = new User();
        user7.setUsername("user7");
        user7.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user7.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user7.setEnabled(true);

        // USER 7
        User user8 = new User();
        user8.setUsername("user8");
        user8.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user8.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user8.setEnabled(true);

        // USER 8
        User user9 = new User();
        user9.setUsername("user9");
        user9.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user9.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user9.setEnabled(true);

        // USER 9
        User user10 = new User();
        user10.setUsername("user10");
        user10.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user10.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user10.setEnabled(true);

        // USER 10
        User user11 = new User();
        user11.setUsername("user11");
        user11.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user11.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        user11.setEnabled(true);

        roles = role2.getUsers();
        if (roles == null) {
            roles = new HashSet<>();
        }

        roles.add(user2);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);
        userRepository.save(user7);
        userRepository.save(user8);
        userRepository.save(user9);
        userRepository.save(user10);
        userRepository.save(user11);

        roleRepository.save(role2);
    }
}

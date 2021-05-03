package pl.pz.oszczedzator3000.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.Role;
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
        for (Role role : Role.values()) {
            pl.pz.oszczedzator3000.model.Role role1 = new pl.pz.oszczedzator3000.model.Role();
            role1.setName("ROLE_" + role.name());
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
        User user2 = new User();
        user2.setUsername("user");
        user2.setPassword(passwordConfig.passwordEncoder().encode("password"));
        user2.setRoles(Stream.of(role2).collect(Collectors.toSet()));
        roles = role2.getUsers();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(user2);
        userRepository.save(user2);
        roleRepository.save(role2);
    }
}

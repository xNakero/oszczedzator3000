package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.config.PasswordConfig;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.exceptions.user.UserAlreadyExistsException;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.AppRole;
import pl.pz.oszczedzator3000.repository.RoleRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordConfig passwordConfig;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordConfig passwordConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
    }

    public void register(UserDto userDto) {
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordConfig.passwordEncoder().encode(userDto.getPassword()));
            Optional<Role> roleOptional = roleRepository.findByName(AppRole.USER.getRoleName());
            roleOptional.ifPresentOrElse(role1 -> {
                        user.setRoles(Set.of(role1));
                        role1.addUser(user);
                        userRepository.save(user);
                    },
                    () -> {
                        Role role = new Role(AppRole.USER.getRoleName());
                        user.setRoles(Set.of(role));
                        role.setUsers(Set.of(user));
                        userRepository.save(user);
                    });
        } else {
            throw new UserAlreadyExistsException(userDto.getUsername());
        }
    }
}

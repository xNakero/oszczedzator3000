package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.config.PasswordConfig;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.exceptions.registration.RegistrationFailedException;
import pl.pz.oszczedzator3000.exceptions.user.UserAlreadyExistsException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotAllowedException;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.AppRole;
import pl.pz.oszczedzator3000.repository.RoleRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;
    private final TokenService tokenService;
    private final MailService mailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordConfig passwordConfig, TokenService tokenService, MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    @Transactional
    public void register(UserDto userDto){
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        User user;
        if (userOptional.isEmpty()) {
            user = new User();
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
        try {
            sendToken(user);
        } catch (MessagingException | MailAuthenticationException e) {
            throw new RegistrationFailedException();
        }
    }

    private void sendToken(User user) throws MessagingException {
        AuthToken token = tokenService.generateToken(user);
        String email = user.getUsername();
        String text = Constants.SERVER_URL + "/api/v1/auth?token=" + token.getValue();
        String subject = "Confirm your email";
        mailService.sendMail(email, subject, text);
    }
}

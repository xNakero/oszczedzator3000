package pl.pz.oszczedzator3000.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.config.PasswordConfig;
import pl.pz.oszczedzator3000.dto.user.UsernameDto;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.exceptions.registration.RegistrationFailedException;
import pl.pz.oszczedzator3000.exceptions.token.InvalidTokenException;
import pl.pz.oszczedzator3000.exceptions.user.UserAlreadyExistsException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.AppRole;
import pl.pz.oszczedzator3000.repository.RoleRepository;
import pl.pz.oszczedzator3000.repository.TokenRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordConfig passwordConfig;
    private final TokenService tokenService;
    private final MailService mailService;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordConfig passwordConfig,
                       TokenService tokenService,
                       MailService mailService,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void register(UserDto userDto) {
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
        sendToken(user);
    }

    public void confirmEmail(String value) {
        AuthToken token = tokenRepository.findByValue(value).orElseThrow(InvalidTokenException::new);
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.deleteById(token.getTokenId());
    }

    public void resendToken(UsernameDto usernameDto) {
        User user = userRepository.findByUsername(usernameDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        sendToken(user);
    }

    private void sendToken(User user){
        AuthToken token = tokenService.generateToken(user);
        String email = user.getUsername();
        String text = Constants.SERVER_URL + "/api/v1/auth?token=" + token.getValue();
        String subject = "Confirm your email";
        mailService.sendMail(email, subject, text);
    }
}

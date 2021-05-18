package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.config.PasswordConfig;
import pl.pz.oszczedzator3000.dto.user.AuthDto;
import pl.pz.oszczedzator3000.dto.user.UsernameDto;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.exceptions.registration.InvalidRegistrationDataException;
import pl.pz.oszczedzator3000.exceptions.token.InvalidTokenException;
import pl.pz.oszczedzator3000.exceptions.user.UserAlreadyExistsException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotAllowedException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.Role;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.AppRole;
import pl.pz.oszczedzator3000.repository.RoleRepository;
import pl.pz.oszczedzator3000.repository.TokenRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

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
            if (!IsUserCredentialsValid(userDto)) {
                throw new InvalidRegistrationDataException();
            }
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

    public void confirmEmail(AuthDto authDto) {
        AuthToken token = tokenRepository.findByValue(authDto.getTokenValue())
                .orElseThrow(() -> new InvalidTokenException("No such token was found."));
        if (!token.getUser().getUsername().equals(authDto.getUsername())) {
            throw new InvalidTokenException("User doesn't match token.");
        }
        if (token.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token Expired.");
        }
        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.deleteById(token.getTokenId());
    }

    public void resendToken(UsernameDto usernameDto) {
        User user = userRepository.findByUsername(usernameDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (user.isEnabled()) {
            throw new UserNotAllowedException();
        }
        sendToken(user);
    }

    private void sendToken(User user) {
        AuthToken token = tokenService.generateToken(user);
        String email = user.getUsername();
        String text = "Your token is: " + token.getValue() + System.lineSeparator() + "It will expire in " +
                +Constants.TOKEN_VALIDATION_MINUTES + " minutes.";
        String subject = "Confirm your email";
        mailService.sendMail(email, subject, text);
    }

    private boolean IsUserCredentialsValid(UserDto userDto) {
        return (userDto.getUsername().matches(Constants.USERNAME_REGEX) &&
                userDto.getPassword().matches(Constants.PASSWORD_REGEX) &&
                userDto.getPassword().length() >= 8 );
    }
}

package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.config.PasswordConfig;
import pl.pz.oszczedzator3000.dto.user.*;
import pl.pz.oszczedzator3000.exceptions.registration.InvalidRegistrationDataException;
import pl.pz.oszczedzator3000.exceptions.token.InvalidTokenException;
import pl.pz.oszczedzator3000.exceptions.user.UserAlreadyExistsException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotAllowedException;
import pl.pz.oszczedzator3000.exceptions.user.UserNotFoundException;
import pl.pz.oszczedzator3000.mapper.UserPersonalDetailsMapper;
import pl.pz.oszczedzator3000.model.*;
import pl.pz.oszczedzator3000.model.enums.AppRole;
import pl.pz.oszczedzator3000.repository.*;

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
    private final AuthTokenRepository authTokenRepository;
    private final JwtSecretRepository jwtSecretRepository;
    private final PasswordChangeTokenRepository passwordChangeTokenRepository;
    private final UserPersonalDetailsMapper userPersonalDetailsMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordConfig passwordConfig,
                       TokenService tokenService,
                       MailService mailService,
                       AuthTokenRepository tokenRepository, JwtSecretRepository jwtSecretRepository, PasswordChangeTokenRepository passwordChangeTokenRepository, UserPersonalDetailsMapper userPersonalDetailsMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordConfig = passwordConfig;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.authTokenRepository = tokenRepository;
        this.jwtSecretRepository = jwtSecretRepository;
        this.passwordChangeTokenRepository = passwordChangeTokenRepository;
        this.userPersonalDetailsMapper = userPersonalDetailsMapper;
    }

    @Transactional
    public void register(RegistrationDto registrationDto) {
        Optional<User> userOptional = userRepository.findByUsername(registrationDto.getUsername());
        User user;
        if (userOptional.isEmpty()) {
            if (!IsUserCredentialsValid(registrationDto) ||
                    registrationDto.getUserPersonalDetailsDto().hasEmptyOrInvalidAttributes()) {
                throw new InvalidRegistrationDataException();
            }
            user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setPassword(passwordConfig.passwordEncoder().encode(registrationDto.getPassword()));
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
            throw new UserAlreadyExistsException(registrationDto.getUsername());
        }
        UserPersonalDetails userPersonalDetails = userPersonalDetailsMapper
                .mapToUserPersonalDetailsWithId(registrationDto.getUserPersonalDetailsDto(), user.getUserId());
        user.setUserPersonalDetails(userPersonalDetails);
        sendAuthToken(user);
    }

    public void confirmEmail(AuthDto authDto) {
        AuthToken token = authTokenRepository.findByValue(authDto.getTokenValue())
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
        authTokenRepository.deleteById(token.getTokenId());
    }

    public void resendToken(UsernameDto usernameDto) {
        User user = userRepository.findByUsername(usernameDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (user.isEnabled()) {
            throw new UserNotAllowedException();
        }
        sendAuthToken(user);
    }

    private void sendAuthToken(User user) {
        AuthToken token = tokenService.generateAuthToken(user);
        String email = user.getUsername();
        String text = "Your token is: " + token.getValue() + System.lineSeparator() + "It will expire in " +
                +Constants.TOKEN_VALIDATION_MINUTES + " minutes.";
        String subject = "Confirm your email";
        mailService.sendMail(email, subject, text);
    }

    private boolean IsUserCredentialsValid(RegistrationDto registrationDto) {
        return (registrationDto.getUsername().matches(Constants.USERNAME_REGEX) &&
                registrationDto.getPassword().matches(Constants.PASSWORD_REGEX) &&
                registrationDto.getPassword().length() >= 8 );
    }

    public void logoutAll() {
        String subject = SecurityContextHolder.getContext().getAuthentication().getName();
        jwtSecretRepository.deleteById(subject);
    }

    public boolean changePassword(PasswordChangeDto passwordChangeDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        if (passwordConfig.passwordEncoder().matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordConfig.passwordEncoder().encode(passwordChangeDto.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void forgotPassword(ForgotPasswordFirstStepDto forgotPasswordFirstStepDto) {
        User user = userRepository.findByUsername(forgotPasswordFirstStepDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        sendPasswordChangeToken(user);

    }

    private void sendPasswordChangeToken(User user) {
        PasswordChangeToken token = tokenService.generatePasswordChangeToken(user);
        String email = user.getUsername();
        String text = "Your password change token is: " + token.getValue() + System.lineSeparator() +
                "It will expire in " + Constants.TOKEN_VALIDATION_MINUTES + " minutes.";
        String subject = "Change your password";
        mailService.sendMail(email, subject, text);
    }

    @Transactional
    public void newPassword(AuthorizationWithPasswordDto authorizationWithPasswordDto) {
        PasswordChangeToken token = passwordChangeTokenRepository.findByValue(authorizationWithPasswordDto.getToken())
                .orElseThrow(() -> new InvalidTokenException("No such token was found."));
        if (!token.getUser().getUsername().equals(authorizationWithPasswordDto.getUsername())) {
            throw new InvalidTokenException("User doesn't match token.");
        }
        if (token.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token Expired.");
        }
        User user = token.getUser();
        user.setPassword(passwordConfig.passwordEncoder().encode(authorizationWithPasswordDto.getPassword()));
        userRepository.save(user);
        passwordChangeTokenRepository.delete(token);
    }

}

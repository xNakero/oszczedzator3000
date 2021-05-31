package pl.pz.oszczedzator3000.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.config.JwtTokenProvider;
import pl.pz.oszczedzator3000.dto.user.UserDto;
import pl.pz.oszczedzator3000.exceptions.token.InvalidTokenException;
import pl.pz.oszczedzator3000.model.JwtSecret;
import pl.pz.oszczedzator3000.repository.JwtSecretRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.util.Base64;

@Service
public class JwtService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JwtSecretRepository jwtSecretRepository;

    @Autowired
    public JwtService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, JwtSecretRepository jwtSecretRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.jwtSecretRepository = jwtSecretRepository;
    }

    public String authenticate(UserDto userAuth) {
        Authentication usernamePasswordAuth = new UsernamePasswordAuthenticationToken(
                userAuth.getUsername(),
                userAuth.getPassword()
        );
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuth);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    public String generateNewToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return jwtTokenProvider.generateTokenExtendDuration(authentication);
    }
}

package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.config.JwtTokenProvider;
import pl.pz.oszczedzator3000.dto.user.UserAuthenticationDto;

@Service
public class AccessService {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AccessService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String authenticate(UserAuthenticationDto userAuth) {
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
        return jwtTokenProvider.generateTokenLogin(authentication);
    }

    public String generateNewToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return jwtTokenProvider.generateTokenExtendDuration(authentication);
    }
}

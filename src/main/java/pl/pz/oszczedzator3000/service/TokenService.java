package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.PasswordChangeToken;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.AuthTokenRepository;
import pl.pz.oszczedzator3000.repository.PasswordChangeTokenRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TokenService {

    private final AuthTokenRepository authTokenRepository;
    private final PasswordChangeTokenRepository passwordChangeTokenRepository;
    private final Random random = new Random(Constants.SEED);

    @Autowired
    public TokenService(AuthTokenRepository tokenRepository, PasswordChangeTokenRepository passwordChangeTokenRepository) {
        this.authTokenRepository = tokenRepository;
        this.passwordChangeTokenRepository = passwordChangeTokenRepository;
    }

    public AuthToken generateAuthToken(User user) {
        String tokenValue = generateToken();
        AuthToken token;
        if (user.getAuthToken() == null) {
            token = new AuthToken();
            token.setUser(user);
        } else {
            token = user.getAuthToken();
        }
        token.setValue(tokenValue);
        LocalDateTime validUntil = LocalDateTime.now();
        validUntil = validUntil.plusMinutes(Constants.TOKEN_VALIDATION_MINUTES);
        token.setValidUntil(validUntil);
        authTokenRepository.save(token);
        return token;
    }

    public PasswordChangeToken generatePasswordChangeToken(User user) {
        String tokenValue = generateToken();
        PasswordChangeToken token;
        if (user.getAuthToken() == null) {
            token = new PasswordChangeToken();
            token.setUser(user);
        } else {
            token = user.getPasswordChangeToken();
        }
        token.setValue(tokenValue);
        LocalDateTime validUntil = LocalDateTime.now();
        validUntil = validUntil.plusMinutes(Constants.TOKEN_VALIDATION_MINUTES);
        token.setValidUntil(validUntil);
        passwordChangeTokenRepository.save(token);
        return token;
    }

    private String generateToken() {
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            tokenBuilder.append(random.nextInt(9));
        }
        return tokenBuilder.toString();
    }

}


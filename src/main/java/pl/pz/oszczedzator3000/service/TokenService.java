package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.Constants;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final Random random = new Random(Constants.SEED);

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public AuthToken generateToken(User user) {
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            tokenBuilder.append(random.nextInt(9));
        }
        String tokenValue = tokenBuilder.toString();
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
        tokenRepository.save(token);
        return token;
    }

}


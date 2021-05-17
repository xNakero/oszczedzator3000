package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.TokenRepository;

import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public AuthToken generateToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        AuthToken token;
        if (user.getAuthToken() == null) {
            token = new AuthToken();
            token.setUser(user);
        } else {
            token = user.getAuthToken();
        }
        token.setValue(tokenValue);
        tokenRepository.save(token);
        return token;
    }

}


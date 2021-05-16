package pl.pz.oszczedzator3000.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.exceptions.token.InvalidTokenException;
import pl.pz.oszczedzator3000.model.AuthToken;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.repository.TokenRepository;
import pl.pz.oszczedzator3000.repository.UserRepository;

import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public AuthToken generateToken(User user) {
        String tokenValue = UUID.randomUUID().toString();
        AuthToken token = new AuthToken();
        token.setValue(tokenValue);
        token.setUser(user);
        tokenRepository.save(token);
        return token;
    }

    public void confirmEmail(String value) {
        try {
            AuthToken token = tokenRepository.findByValue(value).orElseThrow(InvalidTokenException::new);
            User user = token.getUser();
            user.setEnabled(true);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


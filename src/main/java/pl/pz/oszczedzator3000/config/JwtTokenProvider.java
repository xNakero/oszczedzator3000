package pl.pz.oszczedzator3000.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;
import pl.pz.oszczedzator3000.model.JwtSecret;
import pl.pz.oszczedzator3000.service.JwtSecretService;
import pl.pz.oszczedzator3000.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final int EXPIRATION_AFTER = 1000 * 60 * 60 * 24;
    private final JwtSecretService jwtSecretService;

    @Autowired
    public JwtTokenProvider(JwtSecretService jwtSecretService) {
        this.jwtSecretService = jwtSecretService;
    }

    public String generateToken(Authentication authentication) {
        return buildJwt(authentication);
    }

    private String buildJwt(Authentication authentication) {
        long now = System.currentTimeMillis();
        String secret;
        if (jwtSecretService.secretExists(authentication.getName())) {
            secret = jwtSecretService.getFromRedis(authentication.getName()).getSecret();
        } else {
            secret = generateSecret();
            jwtSecretService.saveToRedis(new JwtSecret(authentication.getName(), secret));
        }
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_AFTER))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private String generateSecret() {
        Base64StringKeyGenerator generator = new Base64StringKeyGenerator();
        return generator.generateKey();
    }


}

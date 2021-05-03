package pl.pz.oszczedzator3000.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.pz.oszczedzator3000.model.User;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String SECRET = "Lj1xiAOz/D+E{E%";
    private final int EXPIRATION_AFTER = 1000 * 60 * 60 * 24;

    public String generateTokenLogin(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return buildPartialJwt(authentication)
                .claim("id", user.getUserId())
                .compact();
    }

    public String generateTokenExtendDuration (Authentication authentication) {
        long now = System.currentTimeMillis();
        return buildPartialJwt(authentication)
                .claim("id", authentication.getDetails())
                .compact();
    }

    private JwtBuilder buildPartialJwt(Authentication authentication) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_AFTER))
                .signWith(SignatureAlgorithm.HS256, SECRET);
    }
}

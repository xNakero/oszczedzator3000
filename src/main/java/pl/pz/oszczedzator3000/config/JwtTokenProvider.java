package pl.pz.oszczedzator3000.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.pz.oszczedzator3000.dto.user.UserAuthenticationDto;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final String SECRET = "Lj1xiAOz/D+E{E%";
    private final int EXPIRATION_AFTER = 1000 * 60 * 60 * 24 * 7;

    public String generateToken(Authentication authentication) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_AFTER))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

}

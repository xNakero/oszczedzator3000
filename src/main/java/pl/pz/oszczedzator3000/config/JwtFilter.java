package pl.pz.oszczedzator3000.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtFilter extends BasicAuthenticationFilter {

    private final String SECRET = "Lj1xiAOz/D+E{E%";
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtSecretService = jwtSecretService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        try {
            UsernamePasswordAuthenticationToken authResult = getAuthenticationByToken(header);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            chain.doFilter(request, response);
        } catch (NullPointerException | JwtException e) {
            logger.warn("JwtSecret not found");
            response.setStatus(401);
        }
    }


    private UsernamePasswordAuthenticationToken getAuthenticationByToken(String header) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(header.replace("Bearer ", ""));

        String username = claimsJws.getBody().get("sub").toString();
        String roleString = claimsJws.getBody().get("roles").toString();
        Long id = Long.parseLong(claimsJws.getBody().get("id").toString());
        Set<String> roles = Set.of(roleString.split(","));
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, authorities);
        return usernamePasswordAuthenticationToken;
    }
}

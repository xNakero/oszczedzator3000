package pl.pz.oszczedzator3000.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.pz.oszczedzator3000.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private PasswordConfig passwordConfig;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordConfig passwordConfig) {
        this.userService = userService;
        this.passwordConfig = passwordConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordConfig.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/users/**").hasRole("USER")
                .antMatchers("api/v1/enums/**",
                        "api/v1/login",
                        "api/v1/register").permitAll()
                .and()
                .httpBasic();
    }
}

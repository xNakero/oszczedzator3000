package pl.pz.oszczedzator3000.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthorizationWithPasswordDto {

    private String username;
    private String token;
    @JsonAlias("password")
    private String password;
}

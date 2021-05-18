package pl.pz.oszczedzator3000.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthDto {
    private String username;
    @JsonAlias("token_value")
    private String tokenValue;
}

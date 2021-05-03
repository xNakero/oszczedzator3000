package pl.pz.oszczedzator3000.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAuthenticationDto {

    private String username;
    private String password;
}

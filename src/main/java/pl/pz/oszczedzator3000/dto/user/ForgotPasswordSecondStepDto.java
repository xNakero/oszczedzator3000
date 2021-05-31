package pl.pz.oszczedzator3000.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ForgotPasswordSecondStepDto {

    private String username;
    private String token;
    @JsonAlias("new_password")
    private String newPassword;
}

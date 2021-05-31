package pl.pz.oszczedzator3000.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PasswordChangeLoggedInDto {

    @JsonAlias("old_password")
    private String oldPassword;
    @JsonAlias("new_password")
    private String newPassword;
}

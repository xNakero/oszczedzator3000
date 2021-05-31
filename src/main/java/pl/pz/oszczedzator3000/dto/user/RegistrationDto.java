package pl.pz.oszczedzator3000.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.dto.userpersonaldetails.UserPersonalDetailsDto;

@NoArgsConstructor
@Getter
@Setter
public class RegistrationDto {

    private String username;
    private String password;
    @JsonAlias("user_personal_details")
    private UserPersonalDetailsDto userPersonalDetailsDto;
}

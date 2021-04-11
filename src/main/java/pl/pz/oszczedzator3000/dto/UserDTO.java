package pl.pz.oszczedzator3000.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private LocalDateTime creationDate;
}

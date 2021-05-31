package pl.pz.oszczedzator3000.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_change_token")
@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String value;
    private LocalDateTime validUntil;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

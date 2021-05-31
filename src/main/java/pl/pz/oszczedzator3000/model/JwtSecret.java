package pl.pz.oszczedzator3000.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@RedisHash("secret")
public class JwtSecret {

    @Id
    private String subject;
    private String secret;
}

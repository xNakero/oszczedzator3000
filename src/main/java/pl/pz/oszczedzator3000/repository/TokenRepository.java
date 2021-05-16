package pl.pz.oszczedzator3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pz.oszczedzator3000.model.AuthToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByValue(String value);
}

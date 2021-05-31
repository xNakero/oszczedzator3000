package pl.pz.oszczedzator3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pz.oszczedzator3000.model.PasswordChangeToken;

import java.util.Optional;

public interface PasswordChangeTokenRepository extends JpaRepository<PasswordChangeToken, Long> {

    Optional<PasswordChangeToken> findByValue(String value);
}

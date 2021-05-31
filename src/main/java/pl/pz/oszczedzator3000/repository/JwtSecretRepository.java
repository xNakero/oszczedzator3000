package pl.pz.oszczedzator3000.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.JwtSecret;

import java.util.Optional;

@Repository
public interface JwtSecretRepository extends CrudRepository<JwtSecret, String> {

    Optional<JwtSecret> findBySubject(String subject);
}


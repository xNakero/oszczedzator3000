package pl.pz.oszczedzator3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.UserPersonalDetails;

import java.util.Optional;

@Repository
public interface UserPersonalDetailsRepository extends JpaRepository<UserPersonalDetails, Long> {

    Optional<UserPersonalDetails> findByUser(User user);
}

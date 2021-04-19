package pl.pz.oszczedzator3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.User;

import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Stream<User> streamAllBy();
}

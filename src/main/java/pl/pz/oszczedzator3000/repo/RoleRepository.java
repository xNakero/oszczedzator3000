package pl.pz.oszczedzator3000.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pz.oszczedzator3000.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
package pl.pz.oszczedzator3000.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}

package pl.pz.oszczedzator3000.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.model.User;

import java.util.stream.Stream;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    Stream<Goal> streamAllByUser(User user);

    Page<Goal> findAllByUser(User user, Pageable pageable);
}

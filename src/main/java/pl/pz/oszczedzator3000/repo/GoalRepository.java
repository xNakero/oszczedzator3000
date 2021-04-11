package pl.pz.oszczedzator3000.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.Goal;
import pl.pz.oszczedzator3000.model.User;

import java.util.List;

@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {

    List<Goal> findByUser(User user, Sort sort);

    public static final String FIND_PROJECTS = "SELECT goal_id, category_enum, goal_name, price, target_date FROM goal";
    @Query(value = FIND_PROJECTS, nativeQuery = true)
    public List<Object[]> findProjects();
}

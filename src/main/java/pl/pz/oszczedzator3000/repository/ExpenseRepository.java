package pl.pz.oszczedzator3000.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pz.oszczedzator3000.model.Expense;
import pl.pz.oszczedzator3000.model.User;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.util.stream.Stream;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Stream<Expense> streamAllByUser(User user);

    Page<Expense> findAllByUser(User user, Pageable pageable);
}

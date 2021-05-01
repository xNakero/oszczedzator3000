package pl.pz.oszczedzator3000.dto.expenseoptimiser;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FiltrationExpenseOptimiserRequestDto {

    private boolean salary;
    private boolean profession;
    private boolean age;
    private boolean sex;
    @JsonAlias("relationship_status")
    private boolean relationshipStatus;
    private boolean kids;
    @JsonAlias("start_date")
    private LocalDate startDate;
    @JsonAlias("end_date")
    private LocalDate endDate;
    private Set<Category> categories;
}

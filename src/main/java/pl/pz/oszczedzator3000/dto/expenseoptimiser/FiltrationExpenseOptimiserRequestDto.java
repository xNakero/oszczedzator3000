package pl.pz.oszczedzator3000.dto.expenseoptimiser;

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
    private boolean relationshipStatus;
    private boolean kids;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Category> categories;
}

package pl.pz.oszczedzator3000.dto.expenseoptimiser;

import lombok.*;
import pl.pz.oszczedzator3000.model.enums.Category;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseOptimiserResponseSingleDataDto {

    private Category category;
    private double averageSpent;
    private long expenseCount;
}

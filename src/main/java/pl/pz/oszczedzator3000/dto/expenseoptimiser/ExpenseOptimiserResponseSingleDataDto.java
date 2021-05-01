package pl.pz.oszczedzator3000.dto.expenseoptimiser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.pz.oszczedzator3000.model.enums.Category;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseOptimiserResponseSingleDataDto {

    private Category category;
    @JsonProperty("average_spent")
    private double averageSpent;
    @JsonProperty("expense_count")
    private long expenseCount;
}

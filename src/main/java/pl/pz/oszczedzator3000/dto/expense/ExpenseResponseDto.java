package pl.pz.oszczedzator3000.dto.expense;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseResponseDto {

    @JsonProperty("expense_id")
    private Long expenseId;
    private Category category;
    private String name;
    private double value;
    private LocalDate date;
}

package pl.pz.oszczedzator3000.dto.expense;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseFilterRequestDto {

    private Category category;
    private double minValue;
    private double maxValue;
    private LocalDate startDate;
    private LocalDate endDate;
}

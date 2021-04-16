package pl.pz.oszczedzator3000.dto.expense;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseRequestDto {

    private Category category;
    private String name;
    private double value;
    private LocalDate date;

    public boolean hasInvalidAttributes() {
        return category == null || name == null || value <= 0.0 || date == null;
    }
}

package pl.pz.oszczedzator3000.dto;

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
    private double value;
    private LocalDate startDate;
    private LocalDate endDate;
}

package pl.pz.oszczedzator3000.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseResponseDto {

    private Long expenseId;
    private Category category;
    private String name;
    private double value;
    private LocalDate date;
}

package pl.pz.oszczedzator3000.dto.expense;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonAlias("min_value")
    private double minValue;
    @JsonAlias("max_value")
    private double maxValue;
    @JsonAlias("start_date")
    private LocalDate startDate;
    @JsonAlias("end_date")
    private LocalDate endDate;
    private String name;
}

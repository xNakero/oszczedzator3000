package pl.pz.oszczedzator3000.dto.goal;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.pz.oszczedzator3000.model.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GoalRequestDto {

    private Category category;
    private String name;
    private double price;
    @JsonAlias("target_date")
    private LocalDate targetDate;

    public boolean hasInvalidAttributes() {
        return category == null || name == null || price <= 0.0 || targetDate == null;
    }
}
